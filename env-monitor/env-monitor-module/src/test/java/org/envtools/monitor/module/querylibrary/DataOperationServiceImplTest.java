package org.envtools.monitor.module.querylibrary;

import org.apache.log4j.Logger;
import org.envtools.monitor.model.querylibrary.db.Category;
import org.envtools.monitor.model.querylibrary.db.LibQuery;
import org.envtools.monitor.model.querylibrary.db.QueryExecution;
import org.envtools.monitor.model.updates.DataOperation;
import org.envtools.monitor.model.updates.DataOperationType;
import org.envtools.monitor.module.DataOperationInterface;
import org.envtools.monitor.module.querylibrary.dao.CategoryDao;
import org.envtools.monitor.module.querylibrary.services.DataOperationResult;
import org.envtools.monitor.module.querylibrary.services.DataOperationService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.testng.Assert;

import javax.transaction.*;
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

    private static final String QUERY_TEXT = "SELECT * FROM T WHERE a = '123'";
    private static final String QUERY_SEARCH_PRESENT = "WHERE";
    private static final String QUERY_SEARCH_ABSENT = "WHAT";

    @Test
    public void testDataOperationServiceContains() throws IllegalAccessException, InvocationTargetException, ClassNotFoundException, NoSuchMethodException, InstantiationException, NoSuchFieldException, IntrospectionException, HeuristicRollbackException, HeuristicMixedException, NotSupportedException, RollbackException, SystemException {

        createWithText(QUERY_SEARCH_PRESENT);
        Map<String, String> fields = new HashMap<String, String>();

        fields.put("title", "t1");
        fields.put("description", "test");
        fields.put("owner", "sergey");
        fields.put("parentCategory_id", "1");


        Assert.assertEquals(DataOperationResult.DataOperationStatusE.COMPLETED
                ,dataOperationService.create("Category", fields).getStatus());


        Assert.assertEquals(1,categoryDao.getCategoryByTitle("t1").size());

    }


    @Test
    public void testDataOperationServiceContains1() throws IllegalAccessException, InvocationTargetException, ClassNotFoundException, NoSuchMethodException, InstantiationException, NoSuchFieldException, IntrospectionException, HeuristicRollbackException, HeuristicMixedException, NotSupportedException, RollbackException, SystemException {



        createWithText(QUERY_SEARCH_PRESENT);
        Map<String, String> fields = new HashMap<String, String>();

        fields.put("title", "t2");
        fields.put("description", "test2");
        fields.put("owner", "anastasiya");
        fields.put("parentCategory_id", "f");


        Assert.assertEquals(DataOperationResult.DataOperationStatusE.ERROR
                ,dataOperationService.create("Category", fields).getStatus());


        Assert.assertEquals(0,categoryDao.getCategoryByTitle("t2").size());

    }

    private Category createWithText(String text) {
        Category category = new Category();
        //Don't set Id - it will be auto generated
        category.setTitle(text);
        category.setDescription("some_description");
        category.setOwner("anastasiya");

        Category category1 = new Category();
        //Don't set Id - it will be auto generated
        category1.setTitle("453333");
        category1.setDescription("parent");
        category1.setOwner(null);
        category.setParentCategory(category1);
        category.setChildCategories(category1.getChildCategories());
        categoryDao.saveAndFlush(category1);

        LOGGER.info("parentCategory id " + category1.getId());

       // LibQuery
        LocalDateTime localTime = LocalDateTime.of(2016, 3, 22, 20, 24);
        QueryExecution queryExecution =new QueryExecution();

        queryExecution.setUser("user1");
        //queryExecution.setLibQuery();
        queryExecution.setStartTimestamp(localTime);
        queryExecution.setText("text");

        return categoryDao.saveAndFlush(category);
    }
}
