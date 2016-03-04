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
@TestPropertySource(locations="classpath:/persistence/application-persistence-test.properties")
@Transactional
public class LibQueryDaoIT {

    private static final Logger LOGGER = Logger.getLogger(LibQueryDaoIT.class);

    @Autowired
    LibQueryDao libQueryDao;

    private static final String QUERY_TEXT = "SELECT * FROM T WHERE a = '123'";
    private static final String QUERY_SEARCH_PRESENT = "WHERE";
    private static final String QUERY_SEARCH_ABSENT = "WHAT";

    @Test
    public void testLibQueryContains() {

        Assert.assertTrue(QUERY_TEXT.contains(QUERY_SEARCH_PRESENT));

        createWithText(QUERY_SEARCH_PRESENT);

        List<LibQuery> foundQueries = libQueryDao.getLibQueryByTextFragment(QUERY_SEARCH_PRESENT);
        Assert.assertEquals(1, foundQueries.size());
        Assert.assertEquals(QUERY_TEXT, foundQueries.get(0).getText());

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
        return libQueryDao.saveAndFlush(libQuery);
    }
}
