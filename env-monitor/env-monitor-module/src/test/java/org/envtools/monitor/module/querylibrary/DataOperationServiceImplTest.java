package org.envtools.monitor.module.querylibrary;

import org.apache.log4j.Logger;
import org.envtools.monitor.model.querylibrary.db.Category;
import org.envtools.monitor.model.querylibrary.updates.DataOperation;
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

import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created: 10.04.16 14:45
 *
 * @author Anastasiya Plotnikova
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes =PersistenceTestApplication.class)
@TestPropertySource(locations="classpath:/persistence/application-persistence-test.properties")
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
    public void testDataOperationServiceCreate() throws IllegalAccessException, InvocationTargetException, ClassNotFoundException, NoSuchMethodException, InstantiationException, NoSuchFieldException, IntrospectionException {

        createWithText(QUERY_SEARCH_PRESENT);
        Map<String,String> fields = new HashMap<>();
        fields.put("title","t1");
        fields.put("description","test");
        fields.put("owner","sergey");
        fields.put("parentCategory_id","55");

        Assert.assertEquals(DataOperationResult.DataOperationStatusE.COMPLETED,
                dataOperationService.create("Category",fields).getStatus());
        Assert.assertEquals(1,categoryDao.getCategoryByTitle("t1").size());
    }

    @Test
    public void testDataOperationServiceCreateError() throws IllegalAccessException, InvocationTargetException, ClassNotFoundException, NoSuchMethodException, InstantiationException, NoSuchFieldException, IntrospectionException {

        createWithText(QUERY_SEARCH_PRESENT);
        Map<String,String> fields = new HashMap<String,String>();
        fields.put("title","t1");
        fields.put("description","test");
        fields.put("owner","sergey");
        fields.put("parentCategory_id","t");

        Assert.assertEquals(DataOperationResult.DataOperationStatusE.ERROR,
                dataOperationService.create("Category",fields).getStatus());

    }

    @Test
    public void testDataOperationServiceUpdate() throws IllegalAccessException, InvocationTargetException, ClassNotFoundException, NoSuchMethodException, InstantiationException, NoSuchFieldException, IntrospectionException {

        createWithText(QUERY_SEARCH_PRESENT);
        Map<String,String> fields = new HashMap<String,String>();
        fields.put("title","t5");
        fields.put("description","test");
        fields.put("owner","sergey");
        fields.put("parentCategory_id","1");

        Assert.assertEquals(DataOperationResult.DataOperationStatusE.COMPLETED,
                dataOperationService.update("Category",(long)7,fields).getStatus());
        Assert.assertEquals(1, categoryDao.getCategoryByTitle("t5").size());

    }

    @Test
    public void testDataOperationServiceUpdateError() throws IllegalAccessException, InvocationTargetException, ClassNotFoundException, NoSuchMethodException, InstantiationException, NoSuchFieldException, IntrospectionException {

        createWithText(QUERY_SEARCH_PRESENT);
        Map<String,String> fields = new HashMap<String,String>();
        fields.put("title","t5r");
        fields.put("description","test");
        fields.put("owner","sergey");
        fields.put("parentCategory_id","1");

        Assert.assertEquals(DataOperationResult.DataOperationStatusE.ERROR,
                dataOperationService.update("Category",(long)7,fields).getStatus());
        Assert.assertEquals(0, categoryDao.getCategoryByTitle("t5r").size());

    }

    @Test
    public void testDataOperationServiceDelete() throws IllegalAccessException, InvocationTargetException, ClassNotFoundException, NoSuchMethodException, InstantiationException, NoSuchFieldException, IntrospectionException {

        createWithText(QUERY_SEARCH_PRESENT);
        Map<String,String> fields = new HashMap<String,String>();
        fields.put("title","453333");
        fields.put("description","test");
        fields.put("owner","sergey");
        fields.put("parentCategory_id","1");

       Assert.assertEquals(DataOperationResult.DataOperationStatusE.COMPLETED,
                dataOperationService.delete("Category",(long)5).getStatus());
       Assert.assertEquals(0, categoryDao.getCategoryByTitle("t5").size());

    }



    @Test
    public void testDataOperationServiceDeleteError() throws IllegalAccessException, InvocationTargetException, ClassNotFoundException, NoSuchMethodException, InstantiationException, NoSuchFieldException, IntrospectionException {

        createWithText(QUERY_SEARCH_PRESENT);
        Map<String,String> fields = new HashMap<String,String>();
        fields.put("title","df");
        fields.put("description","test");
        fields.put("owner","sergey");
        fields.put("parentCategory_id","1");

        Assert.assertEquals(DataOperationResult.DataOperationStatusE.ERROR,
                dataOperationService.delete("Category1",(long)7).getStatus());
        Assert.assertEquals(1, categoryDao.getCategoryByTitle("t5").size());

    }

    private Category createWithText(String text){
        Category category = new Category();
        //Don't set Id - it will be auto generated
        category.setTitle("t5");
        category.setDescription("some_description");
        category.setOwner(null);

        Category category1 = new Category();
        //Don't set Id - it will be auto generated
        category1.setTitle("453333");
        category1.setDescription("parent");
        category1.setOwner(null);

        category.setParentCategory(category1);
        categoryDao.saveAndFlush(category1);
        categoryDao.saveAndFlush(category);
        LOGGER.info("id  "+category.getId());
        LOGGER.info("id1  "+category1.getId());
        return categoryDao.saveAndFlush(category);
    }
}
