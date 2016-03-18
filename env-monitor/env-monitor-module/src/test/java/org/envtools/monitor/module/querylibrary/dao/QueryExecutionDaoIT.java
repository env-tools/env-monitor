package org.envtools.monitor.module.querylibrary.dao;

import org.apache.log4j.Logger;
import org.envtools.monitor.model.querylibrary.db.LibQuery;
import org.envtools.monitor.model.querylibrary.db.QueryExecution;
import org.envtools.monitor.module.querylibrary.PersistenceTestApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.testng.Assert;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Created: 10.03.16 22:06
 *
 * @author Anastasiya Plotnikova
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = PersistenceTestApplication.class)
@TestPropertySource(locations = "classpath:/persistence/application-persistence-test.properties")
@Transactional
public class QueryExecutionDaoIT {
    private static final Logger LOGGER = Logger.getLogger(QueryExecutionDaoIT.class);

    @Autowired
    QueryExecutionDao queryExecutionDao;
    @Autowired
    LibQueryDao libQueryDao;

    private static final String QUERY_TEXT = "SELECT * FROM LIB_QUERY WHERE title LIKE '%query%'";
    private static final String QUERY_SEARCH_PRESENT = "WHERE";
    private static final String QUERY_SEARCH_ABSENT = "WHAT";

    @Test
    public void testQueryExecutionContains() {

        Assert.assertTrue(QUERY_TEXT.contains(QUERY_SEARCH_PRESENT));

        createWithText(QUERY_SEARCH_PRESENT);
        LocalDateTime localTimeFrom = LocalDateTime.of(2016, 3, 22, 20, 23);
        LocalDateTime localTimeTo = LocalDateTime.of(2016, 3, 22, 20, 25);

        List<QueryExecution> foundQueries = queryExecutionDao.getByStartTimeInterval(localTimeFrom, localTimeTo);
        Assert.assertEquals(1, foundQueries.size());

        List<QueryExecution> foundQueries1 = queryExecutionDao.getByTextInUserName("admin");
        Assert.assertEquals(1, foundQueries1.size());

        LOGGER.info("Found queries: " + foundQueries);
        LOGGER.info("Found queries 1: " + foundQueries1);

    }

    @Test
    public void testQueryParamNotContains() {

        Assert.assertFalse(QUERY_TEXT.contains(QUERY_SEARCH_ABSENT));
        createWithText(QUERY_SEARCH_ABSENT);
        LocalDateTime localTimeFrom = LocalDateTime.of(2016, 3, 22, 20, 27);
        LocalDateTime localTimeTo = LocalDateTime.of(2016, 3, 22, 20, 29);

        List<QueryExecution> foundQueries3 = queryExecutionDao.getByStartTimeInterval(localTimeFrom, localTimeTo);
        Assert.assertEquals(0, foundQueries3.size());

        List<QueryExecution> foundQueries = queryExecutionDao.getByTextInUserName("Admin");
        Assert.assertEquals(0, foundQueries.size());

        LOGGER.info("Found queries: " + foundQueries);

    }

    private QueryExecution createWithText(String text) {

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
        // queryExecution.set
        return queryExecutionDao.saveAndFlush(queryExecution);
    }
}
