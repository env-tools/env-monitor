package org.envtools.monitor.module.querylibrary.services.filler;

import org.apache.log4j.Logger;

import org.envtools.monitor.model.querylibrary.DataProviderType;
import org.envtools.monitor.model.querylibrary.db.*;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by sergey on 14.03.2016.
 */
@Transactional
public class QueryLibDbFiller {
    private static final int rowCount = 5;

    private static final Logger LOGGER = Logger.getLogger(QueryLibDbFiller.class);

    public static void fillDatabase(EntityManager em, boolean createSampleTestHistory) {
        LOGGER.info("Start to fill database");

        createCategoryTree(em);
        createDataSources(em);

        if (createSampleTestHistory) {
            createSampleTestHistory(em);
        }

        em.flush();
        LOGGER.info("End to fill database");
    }

    public static void createSampleTestHistory(EntityManager em) {
        for (int i = 0; i < rowCount; i++) {
            LibQuery query = (LibQuery) em.createQuery("FROM LibQuery").getResultList().get(0);

            QueryExecution queryExecution = new QueryExecution();
            List<QueryExecutionParam> queryExecutionParams = getQueryExecutionParamList(queryExecution);

            queryExecution.setLibQuery(query);
            queryExecution.setUser("User " + i);
            queryExecution.setStartTimestamp(LocalDateTime.now());
            queryExecution.setEndTimestamp(LocalDateTime.now());
            queryExecution.setText(query.getText());
            queryExecution.setQueryExecutionParams(queryExecutionParams);

            em.persist(queryExecution);
        }
    }

    public static void createDataSources(EntityManager em) {
        for (int i = 0; i < rowCount; i++) {
            DataSource dataSource = new DataSource();
            List<DataSourceProperty> dataSourceProperties = getDataSourcePropertyList();

            dataSource.setName("Source name " + dataSource.hashCode());
            dataSource.setDescription("Source desc " + dataSource.hashCode());
            dataSource.setType(DataProviderType.JDBC);
            dataSource.setDataSourceProperties(dataSourceProperties);

            em.persist(dataSource);
        }
    }

    public static List<QueryExecutionParam> getQueryExecutionParamList(QueryExecution queryExecution) {
        List<QueryExecutionParam> queryExecutionParams = new ArrayList<>();

        QueryExecutionParam queryExecutionParam = new QueryExecutionParam();
        queryExecutionParam.setQueryExecution(queryExecution);
        queryExecutionParam.setName(":id");
        queryExecutionParam.setValue("10");
        queryExecutionParams.add(queryExecutionParam);

        return queryExecutionParams;
    }

    public static List<DataSourceProperty> getDataSourcePropertyList() {
        List<DataSourceProperty> dataSourceProperties = new ArrayList<>();

        DataSourceProperty userName = new DataSourceProperty();
        userName.setProperty("username");
        userName.setValue("Sa");
        dataSourceProperties.add(userName);

        DataSourceProperty password = new DataSourceProperty();
        password.setProperty("password");
        password.setValue("qwerty");
        dataSourceProperties.add(password);

        DataSourceProperty url = new DataSourceProperty();
        url.setProperty("url");
        url.setValue("jdbc:h2:file:./h2_data;MVCC=TRUE;DB_CLOSE_ON_EXIT=FALSE;FILE_LOCK=NO");
        dataSourceProperties.add(url);

        return dataSourceProperties;
    }

    public static void createCategoryTree(EntityManager em) {
        LOGGER.info("Start create the category tree");

        List<Category> publicCategoryList = new ArrayList<>();
        List<Category> privateCategoryList = new ArrayList<>();

        for (int i = 0; i < rowCount; i++) {
            publicCategoryList.add(createPublicCategory());
            privateCategoryList.add(createPrivateCategory());
        }

        publicCategoryList.get(1).setParentCategory(publicCategoryList.get(0));
        publicCategoryList.get(2).setParentCategory(publicCategoryList.get(0));
        publicCategoryList.get(3).setParentCategory(publicCategoryList.get(2));


        privateCategoryList.get(1).setParentCategory(privateCategoryList.get(0));
        privateCategoryList.get(2).setParentCategory(privateCategoryList.get(0));
        privateCategoryList.get(3).setParentCategory(privateCategoryList.get(2));


        saveCategories(em, privateCategoryList);
        saveCategories(em, publicCategoryList);
        LOGGER.info("The category tree was created");
    }

    public static void saveCategories(EntityManager em, List<Category> categories) {
        for (Category category : categories) {
            LibQuery query = createQuery(category);
            QueryParam queryParam = createQueryParam(query);
            em.persist(category);
            em.persist(query);
            em.persist(queryParam);
        }
    }

    public static Category createPublicCategory() {

        Category category = new Category();

        category.setTitle("Title of " + category.hashCode() + " category.");
        category.setDescription("Desc of " + category.hashCode() + " category.");

        return category;
    }

    public static Category createPrivateCategory() {
        Category category = createPublicCategory();

        category.setOwner(UUID.randomUUID().toString());

        return category;
    }

    public static LibQuery createQuery(Category category) {
        LibQuery query = new LibQuery();

        query.setTitle("Title of " + query.hashCode() + " query.");
        query.setDescription("Desc of " + query.hashCode() + " query.");
        query.setText("SELECT * FROM LIB_QUERY WHERE id = :id");
        query.setCategory(category);

        return query;
    }

    public static QueryParam createQueryParam(LibQuery query) {
        QueryParam queryParam = new QueryParam();

        queryParam.setName(":id");
        queryParam.setLibQuery(query);
        queryParam.setType(DataProviderType.JDBC);

        return queryParam;
    }
}
