package org.envtools.monitor.module.querylibrary.services.export;

import org.envtools.monitor.model.querylibrary.QueryParamType;
import org.envtools.monitor.model.querylibrary.db.Category;
import org.envtools.monitor.model.querylibrary.db.LibQuery;
import org.envtools.monitor.model.querylibrary.db.QueryParam;
import org.envtools.monitor.module.querylibrary.PersistenceTestApplication;
import org.envtools.monitor.module.querylibrary.dao.CategoryDao;
import org.envtools.monitor.module.querylibrary.dao.LibQueryDao;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.net.URI;
import java.nio.file.*;
import java.util.Collections;

/**
 * Created by IAvdeev on 18.01.2017.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = PersistenceTestApplication.class)
@TestPropertySource(locations = "classpath:/persistence/application-persistence-test.properties")
@Transactional
public class ZipArchiveExportServiceTest {

    @Autowired
    LibQueryDao libQueryDao;

    @Autowired
    CategoryDao categoryDao;

    @Autowired
    ZipArchiveExportService exportService;

    @Test
    public void testCreateArchive() throws Exception {

        bootstrapTestQueries();

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        exportService.writeArchive(stream);

        Path tempFile = null;
        try {
            // save .zip file in order to use FileSystems.newFileSystem for testing
            tempFile = Files.createTempFile("tempfile", ".zip");
            try (OutputStream outputStream = Files.newOutputStream(tempFile)) {
                stream.writeTo(outputStream);
            }

            String s = "jar:" + tempFile.toUri();
            //s = "jar:file:///c:/archive.zip";
            try (FileSystem zipFs = FileSystems.newFileSystem(new URI(s), Collections.emptyMap())) {

                Files.exists(zipFs.getPath("/Root category 1"));
                Files.exists(zipFs.getPath("/Root category 1/Child category 2"));
                Files.exists(zipFs.getPath("/Root category 1/Child category 2/Query 1.sql"));

                try (BufferedReader bufferedReader = Files.newBufferedReader(zipFs.getPath("/Root category 1/Child category 2/Query 1.sql"))) {
                    Assert.assertEquals("--TITLE Query 1", bufferedReader.readLine());
                    Assert.assertEquals("--DESCRIPTION Description", bufferedReader.readLine());
                    Assert.assertEquals("--PARAM a:NUMBER:1", bufferedReader.readLine());
                    Assert.assertEquals("--PARAM b:NUMBER", bufferedReader.readLine());
                    Assert.assertEquals("", bufferedReader.readLine());
                    Assert.assertEquals("SELECT :a + :b", bufferedReader.readLine());
                }
            }


        } finally {
            // delete temp zip file if exists
            if (tempFile != null) {
                Files.delete(tempFile);
            }
        }

        Assert.assertTrue(true);
    }

    private void bootstrapTestQueries() {
        Category rootCat = new Category();
        rootCat.setTitle("Root category '1'");

        Category childCat = new Category();
        childCat.setTitle("Child category '2'");
        childCat.setParentCategory(rootCat);


        LibQuery query1 = new LibQuery();
        query1.setTitle("Query 1");
        query1.setText("SELECT :a + :b");
        query1.setDescription("Description");
        query1.addQueryParam(new QueryParam("a", QueryParamType.NUMBER, "1"));
        query1.addQueryParam(new QueryParam("b", QueryParamType.NUMBER, null));
        query1.setCategory(childCat);


        categoryDao.saveAndFlush(rootCat);
    }

}