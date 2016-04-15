package org.envtools.monitor.module.querylibrary.dao;

import org.apache.log4j.Logger;
import org.envtools.monitor.model.querylibrary.db.LibQuery;
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
 * Created: 05.03.16 3:33
 *
 * @author Yury Yakovlev
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = PersistenceTestApplication.class)
@TestPropertySource(locations = "classpath:/persistence/application-persistence-test.properties")
@Transactional
public class LibQueryDaoIT {

    private static final Logger LOGGER = Logger.getLogger(LibQueryDaoIT.class);

    @Autowired
    LibQueryDao libQueryDao;

    private static final String QUERY_TEXT = "SELECT * FROM LIB_QUERY WHERE title LIKE '%query%'";
    private static final String QUERY_SEARCH_PRESENT = "WHERE";
    private static final String QUERY_SEARCH_ABSENT = "WHAT";

    @Test
    public void testLibQueryContains() {

        Assert.assertTrue(QUERY_TEXT.contains(QUERY_SEARCH_PRESENT));

        createWithText(QUERY_SEARCH_PRESENT);

        List<LibQuery> foundQueries = libQueryDao.getLibQueryByTextFragment(QUERY_SEARCH_PRESENT);
        Assert.assertEquals(1, foundQueries.size());
        System.out.println(QUERY_TEXT);
        List<LibQuery> foundQueries1 = libQueryDao.getLibQueryByTextFragment("LIB_QUERY");
        Assert.assertEquals(0, foundQueries1.size());
        List<LibQuery> foundQueries2 = libQueryDao.getLibQueryByTextFragment("123");
        Assert.assertEquals(1, foundQueries2.size());
        Assert.assertEquals(QUERY_SEARCH_PRESENT, foundQueries.get(0).getText());

        LOGGER.info("Found queries: " + foundQueries);

    }

    @Test
    public void testLibQueryNotContains() {

        Assert.assertFalse(QUERY_TEXT.contains(QUERY_SEARCH_ABSENT));

        createWithText(QUERY_SEARCH_ABSENT);

        List<LibQuery> foundQueries = libQueryDao.getLibQueryByTextFragment(QUERY_SEARCH_PRESENT);
        Assert.assertEquals(0, foundQueries.size());

        LOGGER.info("Found queries: " + foundQueries);

    }

    private LibQuery createWithText(String text) {
        LibQuery libQuery = new LibQuery();
        //Don't set Id - it will be auto generated
        libQuery.setText(text);
        libQuery.setDescription("some_description");
        libQuery.setTitle("some_title");

        LibQuery libQuery1 = new LibQuery();
        //Don't set Id - it will be auto generated
        libQuery1.setText("123");
        libQuery1.setDescription("some_description1");
        libQuery1.setTitle("some_title1");
        libQueryDao.saveAndFlush(libQuery1);
        return libQueryDao.saveAndFlush(libQuery);
    }
}
