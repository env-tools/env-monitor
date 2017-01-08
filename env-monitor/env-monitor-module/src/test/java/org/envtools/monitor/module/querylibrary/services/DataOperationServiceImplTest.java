package org.envtools.monitor.module.querylibrary.services;

import org.apache.log4j.Logger;
import org.envtools.monitor.model.querylibrary.DataProviderType;
import org.envtools.monitor.model.querylibrary.db.*;
import org.envtools.monitor.model.querylibrary.updates.DataOperation;
import org.envtools.monitor.module.exception.DataOperationException;
import org.envtools.monitor.module.querylibrary.PersistenceTestApplication;
import org.envtools.monitor.module.querylibrary.dao.CategoryDao;
import org.envtools.monitor.module.querylibrary.dao.DataSourceDao;
import org.envtools.monitor.module.querylibrary.dao.LibQueryDao;
import org.envtools.monitor.module.querylibrary.dao.QueryExecutionDao;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Created: 10.04.16 14:45
 *
 * @author Anastasiya Plotnikova
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = PersistenceTestApplication.class)
@TestPropertySource(locations = "classpath:/persistence/application-persistence-test.properties")
@Transactional
public class DataOperationServiceImplTest {
    private static final Logger LOGGER = Logger.getLogger(DataOperationServiceImplTest.class);

    @Autowired
    CategoryDao categoryDao;

    @Autowired
    DataOperationService dataOperationService;

    @Autowired
    DataOperation dataOperation;

    @Autowired
    DataSourceDao dataSourceDao;

    @Autowired
    QueryExecutionDao queryExecutionDao;

    @Autowired
    LibQueryDao libQueryDao;

    private static final String QUERY_TEXT = "SELECT * FROM T WHERE a = '123'";
    private static final String QUERY_SEARCH_PRESENT = "WHERE";
    private static final String QUERY_SEARCH_ABSENT = "WHAT";

    @Test
    public void testDataOperationServiceCreate() throws Exception {

        Category parentCategory = createWithText(QUERY_SEARCH_PRESENT);

        Map<String, String> fields = new HashMap<>();
        fields.put("title", "t1");
        fields.put("description", "test");
        fields.put("owner", "sergey");
        fields.put("parentCategory_id", String.valueOf(parentCategory.getId()));

        Assert.assertEquals(0, categoryDao.getCategoryByTitle("t1").size());

        Assert.assertEquals(0, categoryDao.getChildCategoriesByParentId(4L).size());


        Assert.assertEquals(DataOperationResult.DataOperationStatusE.COMPLETED,
                dataOperationService.create("Category", fields).getStatus());

        Assert.assertEquals(1, categoryDao.getCategoryByTitle("t1").size());

        Assert.assertEquals(1, categoryDao.getChildCategoriesByParentId(parentCategory.getId()).size());

    }

    @Test
    public void testDataOperationServiceCreateError() throws Exception {

        createWithText(QUERY_SEARCH_PRESENT);
        Map<String, String> fields = new HashMap<String, String>();
        fields.put("title", "t1");
        fields.put("description", "test");
        fields.put("owner", "sergey");
        fields.put("parentCategory_id", "1");

        Assert.assertEquals(DataOperationResult.DataOperationStatusE.ERROR,
                dataOperationService.create("Category1", fields).getStatus());

    }

    @Test
    public void testDataOperationServiceUpdate() throws Exception {

        Category childCategory = createWithText(QUERY_SEARCH_PRESENT);
        Category parentCategory = createWithText(QUERY_SEARCH_PRESENT);

        Map<String, String> fields = new HashMap<String, String>();
        fields.put("title", "t6");
        fields.put("description", "test");
        fields.put("owner", "sergey");
        fields.put("parentCategory_id", String.valueOf(parentCategory.getId()));
        Assert.assertEquals(0, categoryDao.getChildCategoriesByParentId(parentCategory.getId()).size());
        Assert.assertEquals(DataOperationResult.DataOperationStatusE.COMPLETED,
                dataOperationService.update("Category", childCategory.getId(), fields).getStatus());
        Assert.assertEquals(1, categoryDao.getCategoryByTitle("t6").size());
        Assert.assertEquals(1, categoryDao.getChildCategoriesByParentId(parentCategory.getId()).size());

    }

    @Test
    public void testDataOperationServiceUpdateError() throws Exception {

        createWithText(QUERY_SEARCH_PRESENT);
        Map<String, String> fields = new HashMap<String, String>();
        fields.put("title", "t5r");
        fields.put("description", "test");
        fields.put("owner", "sergey");
        fields.put("parentCategory_id", "t1");

        Assert.assertEquals(DataOperationResult.DataOperationStatusE.ERROR,
                dataOperationService.update("Category1", (long) 9, fields).getStatus());
        Assert.assertEquals(0, categoryDao.getCategoryByTitle("t5r").size());

    }

    @Test
    public void testDataOperationServiceDelete() throws Exception {

        Category parentCategory = createWithText(QUERY_SEARCH_PRESENT);

        Assert.assertEquals(1, categoryDao.getCategoryByTitle("t5").size());
        Assert.assertEquals(0, categoryDao.getChildCategoriesByParentId(parentCategory.getId()).size());
        Assert.assertEquals(DataOperationResult.DataOperationStatusE.COMPLETED,
                dataOperationService.delete("Category", parentCategory.getId()).getStatus());
        Assert.assertEquals(0, categoryDao.getCategoryByTitle("t5").size());
        Assert.assertEquals(0, categoryDao.getChildCategoriesByParentId(parentCategory.getId()).size());

    }


    @Test
    public void testDataOperationServiceDeleteError() throws Exception {

        createWithText(QUERY_SEARCH_PRESENT);
        Map<String, String> fields = new HashMap<String, String>();
        fields.put("title", "df");
        fields.put("description", "test");
        fields.put("owner", "sergey");
        fields.put("parentCategory_id", "3");

        Assert.assertEquals(DataOperationResult.DataOperationStatusE.ERROR,
                dataOperationService.delete("Category1", (long) 3).getStatus());
        Assert.assertEquals(1, categoryDao.getCategoryByTitle("t5").size());

    }

    @Test
    public void testDataOperationServiceCreateDataSourse() throws Exception {

        createDataSource("sdf");
        Map<String, String> fields = new HashMap<String, String>();
        fields.put("name", "df");
        fields.put("description", "test");
        fields.put("type", "JDBC");

        Assert.assertEquals(DataOperationResult.DataOperationStatusE.COMPLETED,
                dataOperationService.create("DataSource", fields).getStatus());
        Assert.assertEquals(1, dataSourceDao.getNameByText("df").size());

    }

    @Test
    public void testDataOperationServiceCreateQueryExecution() throws Exception {

        createQueryExecution("sdf");
        Map<String, String> fields = new HashMap<String, String>();
        fields.put("endTimestamp", "2017-03-22 21:24");
        fields.put("startTimestamp", "2017-03-22 20:24");
        fields.put("user", "USER");

        LocalDateTime localTime = LocalDateTime.of(2017, 3, 22, 20, 24);
        LocalDateTime localTime1 = LocalDateTime.of(2017, 3, 22, 21, 24);

        Assert.assertEquals(DataOperationResult.DataOperationStatusE.COMPLETED,
                dataOperationService.create("QueryExecution", fields).getStatus());
        Assert.assertEquals(1, queryExecutionDao.getByTextInUserName("USER").size());
        Assert.assertEquals(1, queryExecutionDao.getByStartTimeInterval(localTime, localTime1).size());

    }


    private Category createWithText(String text) {
        Category category = new Category();
        //Don't set Id - it will be auto generated
        category.setTitle("t5");
        category.setDescription("some_description");
        category.setOwner(null);

       // Category category1 = new Category();
        //Don't set Id - it will be auto generated
       // category1.setTitle("453333");
       // category1.setDescription("parent");
      //  category1.setOwner(null);

       // category.setParentCategory(category1);
       // categoryDao.saveAndFlush(category1);
        categoryDao.saveAndFlush(category);
        LOGGER.info("id  родительский " + category.getId());
       // LOGGER.info("id  родительский  " + category1.getId());
        return categoryDao.saveAndFlush(category);
    }


    private DataSource createDataSource(String text) {
        DataSource dataSource = new DataSource();

        dataSource.setDescription("JDBC");
        dataSource.setName("name");
        dataSource.setType(DataProviderType.JDBC);
        LOGGER.info("id dataSource  " + dataSource.getId());
        return dataSourceDao.saveAndFlush(dataSource);
    }


    private QueryExecution createQueryExecution(String text) {

        LibQuery libQuery1 = new LibQuery();
        //Don't set Id - it will be auto generated
        libQuery1.setText("123");
        libQuery1.setDescription("some_description1");
        libQuery1.setTitle("some_title1");
        libQueryDao.saveAndFlush(libQuery1);

        LocalDateTime localTime = LocalDateTime.of(2016, 3, 22, 20, 24);
        QueryExecution queryExecution = new QueryExecution();
        //Don't set Id - it will be auto generated
        queryExecution.setEndTimestamp(localTime);
        queryExecution.setStartTimestamp(localTime);
        queryExecution.setLibQuery(libQuery1);
        queryExecution.setUser("admin");
        LOGGER.info("LocalDateTime " + localTime);
        LOGGER.info("id dataSource  " + queryExecution.getId());
        return queryExecutionDao.saveAndFlush(queryExecution);
    }
}
