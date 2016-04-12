package org.envtools.monitor.module.querylibrary;

import org.apache.log4j.Logger;
import org.envtools.monitor.model.querylibrary.db.Category;
import org.envtools.monitor.model.updates.DataOperation;
import org.envtools.monitor.model.updates.DataOperationType;
import org.envtools.monitor.module.DataOperationInterface;
import org.envtools.monitor.module.querylibrary.dao.CategoryDao;
import org.envtools.monitor.module.querylibrary.services.DataOperationService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

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
    public void testDataOperationServiceContains() throws IllegalAccessException, InvocationTargetException, ClassNotFoundException, NoSuchMethodException, InstantiationException, NoSuchFieldException, IntrospectionException {

        createWithText(QUERY_SEARCH_PRESENT);
        Map<String,String> fields = new HashMap<String,String>();

        fields.put("Title","t1");
        fields.put("Description","test");
        fields.put("Owner","sergey");
        fields.put("ParentCategory_id","1");

        //dataOperation.setEntity("Category");
       // dataOperation.setType(DataOperationType.UPDATE);
       // dataOperation.setFields(fields);
        dataOperationService.create("Category",fields);

    }



    @Test
    public void testDataOperationServiceContains1() throws IllegalAccessException, InvocationTargetException, ClassNotFoundException, NoSuchMethodException, InstantiationException, NoSuchFieldException {

       // createWithText(QUERY_SEARCH_PRESENT);

       // Map<String,String> fields1 = new HashMap<String,String>();
       // fields1.put("text","text");
      //  fields1.put("description","test");
      //  fields1.put("title","sssergey");
       // fields1.put("category_id","77");

       // dataOperation.setEntity("LibQuery");
      //  dataOperation.setType(DataOperationType.UPDATE);
      //  dataOperation.setFields(fields1);
      //  dataOperationInterface.create(dataOperation);

    }

    private Category createWithText(String text){
        Category category = new Category();
        //Don't set Id - it will be auto generated
        category.setTitle(text);
        category.setDescription("some_description");
        category.setOwner(null);

        Category category1 = new Category();
        //Don't set Id - it will be auto generated
        category1.setTitle("453333");
        category1.setDescription("parent");
        category1.setOwner(null);
        category.setParentCategory(category1);
        categoryDao.saveAndFlush(category1);
        LOGGER.info("parentCategory id "+category1.getId());
        return categoryDao.saveAndFlush(category);
    }
}
