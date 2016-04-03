package org.envtools.monitor.module.querylibrary.dao;

import org.apache.log4j.Logger;
import org.envtools.monitor.model.querylibrary.db.Category;

import org.envtools.monitor.module.querylibrary.PersistenceTestApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.testng.Assert;

import java.util.List;

/**
 * Created: 06.03.16 3:33
 *
 * @author Anastasiya
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes =PersistenceTestApplication.class)
@TestPropertySource(locations="classpath:/persistence/application-persistence-test.properties")
@Transactional
public class CategoryDaoIT {

    private static final Logger LOGGER = Logger.getLogger(CategoryDaoIT.class);

    @Autowired
    CategoryDao categoryDao;

    private static final String QUERY_TEXT = "SELECT * FROM T WHERE a = '123'";
    private static final String QUERY_SEARCH_PRESENT = "WHERE";
    private static final String QUERY_SEARCH_ABSENT = "WHAT";

    @Test
    public void testCategoryContains() {

        Assert.assertTrue(QUERY_TEXT.contains(QUERY_SEARCH_PRESENT));
        createWithText(QUERY_SEARCH_PRESENT);
        List<Category> foundQueries = categoryDao.getCategoryByTitle(QUERY_SEARCH_PRESENT);
        Assert.assertEquals(1, foundQueries.size());
        Assert.assertEquals(QUERY_SEARCH_PRESENT, foundQueries.get(0).getTitle());
        List<Category> foundQueries1 = categoryDao.getRootCategories();
        Assert.assertEquals(1, foundQueries1.size());
       // List<Category> foundQueries2 = categoryDao.getRootCategoriesByOwner("owner");
      //  Assert.assertEquals(1, foundQueries2.size());
        List<Category> foundQueries3 = categoryDao.getRootCategoriesByOwner(null);
        Assert.assertEquals(1, foundQueries3.size());

        LOGGER.info("Found queries: " + foundQueries);
        LOGGER.info("Found queries: " + foundQueries1);
        //LOGGER.info("Found queries: " + foundQueries2);
       // LOGGER.info("Found queries: " + foundQueries3);

    }

    @Test
    public void testCategoryNotContains() {

        Assert.assertFalse(QUERY_TEXT.contains(QUERY_SEARCH_ABSENT));

        createWithText(QUERY_SEARCH_ABSENT);

        List<Category> foundQueries = categoryDao.getCategoryByTitle(QUERY_SEARCH_ABSENT);
        Assert.assertEquals(1, foundQueries.size());

        LOGGER.info("Found queries: " + foundQueries);
    }

    private Category createWithText(String text) {
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
