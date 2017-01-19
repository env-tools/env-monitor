package org.envtools.monitor.module.querylibrary.services.bootstrap;

import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;
import org.envtools.monitor.model.querylibrary.QueryParamType;
import org.envtools.monitor.model.querylibrary.db.Category;
import org.envtools.monitor.model.querylibrary.db.LibQuery;
import org.envtools.monitor.model.querylibrary.db.QueryParam;
import org.envtools.monitor.module.exception.BootstrapZipParseException;
import org.envtools.monitor.module.querylibrary.dao.CategoryDao;
import org.envtools.monitor.module.querylibrary.dao.LibQueryDao;
import org.envtools.monitor.module.querylibrary.dao.QueryParamDao;
import org.envtools.monitor.module.querylibrary.services.BootstrapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URI;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


/**
 * Created by IAvdeev on 11.01.2017.
 * Load the categories and queries from zip archive, which contains sql files in directories hierarchy.
 *
 * @example of file structure in zip
 * <code>
 * Order
 * +- List Orders
 * + - List 10 latest orders.sql
 * |   <file-contents>
 * |     --DESCRIPTION: My description
 * |
 * |      SELECT * FROM orders ORDER BY date LIMIT 10
 * |   </file-contents>
 * + - Get order by id.sql
 * |   <file-contents>
 * |     --DESCRIPTION My description
 * |     --PARAM id:NUMBER
 * |
 * |     SELECT * FROM orders WHERE id = :id
 * |   </file-contents>
 * </code>
 */
@Service
@Transactional
@ConditionalOnExpression("'${querylibrary.query_bootstrap_location:}' matches '^.+?\\.zip$'")
public class ZipArchiveBootstrapService implements BootstrapService {
    private static final Logger LOGGER = Logger.getLogger(ZipArchiveBootstrapService.class);

    @Value("${querylibrary.query_bootstrap_location:}")
    Resource zipFileResource;

    @PersistenceContext
    protected EntityManager em;

    @Autowired
    CategoryDao categoryDao;

    @Autowired
    QueryParamDao queryParamDao;

    @Autowired
    LibQueryDao libQueryDao;

    /**
     * Opens an zip file at location `fileLocation` and populates the database by reading all sql files,
     * using directory hierarchies as categories.
     *
     * @throws Exception
     */
    @Override
    public void bootstrap() throws Exception {
        URI uri = URI.create("jar:" + zipFileResource.getURI());
        Map<String, String> env = new HashMap<>();

        try (FileSystem zipFileSystem = FileSystems.newFileSystem(uri, env)) {
            Path root = zipFileSystem.getPath("/");

            //walk the zip file tree and copy files to the destination
            Files.walkFileTree(root, new SimpleFileVisitor<Path>() {
                private Category currentCategory = null;

                @Override
                public FileVisitResult visitFile(Path file,
                                                 BasicFileAttributes attrs) throws IOException {
                    if (FilenameUtils.isExtension(file.toString(), "sql")) {
                        LOGGER.info(String.format("Load query from %s", file.toString()));
                        createQuery(file, currentCategory);
                    }
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult preVisitDirectory(Path dir,
                                                         BasicFileAttributes attrs) throws IOException {
                    // skip very first "/" directory
                    if (dir.getParent() != null) {
                        // get rid of ending slash
                        String title = FilenameUtils.getPathNoEndSeparator(dir.getFileName().toString());
                        currentCategory = createCategory(title, currentCategory);
                    }
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                    if (currentCategory != null) {
                        currentCategory = currentCategory.getParentCategory();
                    }
                    return FileVisitResult.CONTINUE;
                }
            });
        }
    }

    private LibQuery createQuery(Path path, Category category) throws IOException, BootstrapZipParseException {
        LOGGER.debug(String.format("Reading file '%s'", path.toString()));

        LibQuery libQuery = new LibQuery();
        libQuery.setCategory(category);
        // by default use filename as title for queries
        libQuery.setTitle(FilenameUtils.getBaseName(path.toString()));

        boolean rawSqlPart = false;

        try (BufferedReader reader = Files.newBufferedReader(path);
             StringWriter rawSqlString = new StringWriter();
             PrintWriter rawSqlWr = new PrintWriter(rawSqlString)) {
            String line;

            Pattern pattern = Pattern.compile("^--(TITLE|DESCRIPTION|PARAM)\\s+(.+)$");

            for (int lineNumber = 1; (line = reader.readLine()) != null; lineNumber++) {
                Matcher matcher = pattern.matcher(line);
                // break at first non-keyword name
                if (!rawSqlPart && matcher.matches()) {

                    String name = matcher.group(1);
                    String value = matcher.group(2).trim();

                    switch (name) {
                        case "TITLE":
                            libQuery.setTitle(value);
                            break;
                        case "DESCRIPTION":
                            libQuery.setDescription(value);
                            break;
                        case "PARAM":
                            String[] nameTypeAndDefaultValue = value.split(":", 3);
                            if (nameTypeAndDefaultValue.length < 2) {
                                throw new BootstrapZipParseException(String.format("Unexpected param format '%s' at line %d in '%s'", line, lineNumber, path.getFileName().toString()));
                            }

                            String paramName = nameTypeAndDefaultValue[0];
                            String paramType = nameTypeAndDefaultValue[1];
                            String defaultValue = nameTypeAndDefaultValue.length > 2 ? nameTypeAndDefaultValue[2] : null;

                            Collection<String> availableParamsTypes = Arrays.stream(QueryParamType.values())
                                    .map(Enum::name)
                                    .collect(Collectors.toList());

                            if (!availableParamsTypes.contains(paramType)) {
                                throw new BootstrapZipParseException(String.format("Unexpected param type '%s' at line %d in '%s'",
                                        line, lineNumber, path.getFileName().toString()));
                            }

                            QueryParam queryParam = new QueryParam(
                                    paramName,
                                    QueryParamType.valueOf(paramType),
                                    defaultValue);

                            libQuery.addQueryParam(queryParam);

                            break;
                        default:
                            throw new BootstrapZipParseException(String.format("Unexpected param '%s' at line %d in '%s'", line, lineNumber, path.getFileName().toString()));
                    }
                } else {
                    // !matcher.matches()
                    rawSqlPart = true;
                    rawSqlWr.println(line);
                }
            }

            libQuery.setText(rawSqlString.toString().trim());

            if (libQuery.getDescription() == null) {
                libQuery.setDescription(libQuery.getTitle());
            }
        }
        libQueryDao.save(libQuery);

        return libQuery;
    }

    private Category createCategory(String title, Category parentCat) {
        Category cat = new Category();
        cat.setQueries(new ArrayList<>());
        cat.setTitle(title);
        cat.setParentCategory(parentCat);
        categoryDao.saveAndFlush(cat);

        return cat;
    }
}
