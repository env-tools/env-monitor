package org.envtools.monitor.module.querylibrary;

import org.apache.log4j.Logger;
import org.envtools.monitor.model.querylibrary.db.Category;
import org.envtools.monitor.module.DataOperationInterface;
import org.envtools.monitor.module.querylibrary.dao.CategoryDao;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.testng.Assert;
import org.envtools.monitor.model.querylibrary.db.Category;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * Created: 10.04.16 14:45
 *
 * @author Anastasiya Plotnikova
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes =PersistenceTestApplication.class)
@TestPropertySource(locations="classpath:/persistence/application-persistence-test.properties")
@Transactional
public class DataOperationServiceTest {
    private static final Logger LOGGER = Logger.getLogger(DataOperationServiceTest.class);

    @Autowired
    CategoryDao categoryDao;

    @Autowired
    DataOperationInterface dataOperationInterface;

    private static final String QUERY_TEXT = "SELECT * FROM T WHERE a = '123'";
    private static final String QUERY_SEARCH_PRESENT = "WHERE";
    private static final String QUERY_SEARCH_ABSENT = "WHAT";
    @Test
    public void testDataOperationServiceContains() throws IllegalAccessException, InvocationTargetException, ClassNotFoundException, NoSuchMethodException, InstantiationException {

        createWithText(QUERY_SEARCH_PRESENT);
        dataOperationInterface.create("(\"Category\" ,title:\"t1\")");

        dataOperationInterface.create("(\"LibQuery\" ,title:\"t1\")");

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
        return categoryDao.saveAndFlush(category);
    }
}
