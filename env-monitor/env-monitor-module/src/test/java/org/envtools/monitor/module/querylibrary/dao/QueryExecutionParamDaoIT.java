package org.envtools.monitor.module.querylibrary.dao;

import org.apache.log4j.Logger;
import org.envtools.monitor.model.querylibrary.db.LibQuery;
import org.envtools.monitor.model.querylibrary.db.QueryExecution;
import org.envtools.monitor.model.querylibrary.db.QueryExecutionParam;
import org.envtools.monitor.module.querylibrary.PersistenceTestApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.testng.Assert;

import java.time.LocalTime;
import java.util.List;

/**
 * Created: 10.03.16 22:46
 *
 * @author Anastasiya Plotnikova
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = PersistenceTestApplication.class)
@TestPropertySource(locations="classpath:/persistence/application-persistence-test.properties")
@Transactional
public class QueryExecutionParamDaoIT {
    private static final Logger LOGGER = Logger.getLogger(QueryExecutionDaoIT.class);

    @Autowired
    QueryExecutionParamDao queryExecutionParamDao;
    @Autowired
    QueryExecutionDao queryExecutionDao;

    private static final String QUERY_TEXT = "SELECT * FROM LIB_QUERY WHERE title LIKE '%query%'";
    private static final String QUERY_SEARCH_PRESENT = "WHERE";
    private static final String QUERY_SEARCH_ABSENT = "WHAT";

    @Test
    public void testQueryExecutionParamContains() {

        Assert.assertTrue(QUERY_TEXT.contains(QUERY_SEARCH_PRESENT));

        createWithText(QUERY_SEARCH_PRESENT);

        List<QueryExecutionParam> foundQueries = queryExecutionParamDao.getValueByText("public");
        Assert.assertEquals(1, foundQueries.size());


        List<QueryExecutionParam> foundQueries1 = queryExecutionParamDao.getNameByText("Андрейко");
        Assert.assertEquals(1, foundQueries1.size());


        LOGGER.info("Found queries: " + foundQueries);
        LOGGER.info("Found queries: " + foundQueries1);

    }

    @Test
    public void testQueryExecutionParamNotContains() {

        createWithText(QUERY_SEARCH_ABSENT);

        List<QueryExecutionParam> foundQueries = queryExecutionParamDao.getValueByText("publics");
        Assert.assertEquals(0, foundQueries.size());


        List<QueryExecutionParam> foundQueries1 = queryExecutionParamDao.getNameByText("sdf");
        Assert.assertEquals(0, foundQueries1.size());

        LOGGER.info("Found queries: " + foundQueries);
        LOGGER.info("Found queries: " + foundQueries1);
    }

    private QueryExecutionParam createWithText(String text) {

        QueryExecution queryExecution = new QueryExecution();
        //Don't set Id - it will be auto generated

    queryExecution.setUser("Андрейко");
       queryExecutionDao.saveAndFlush(queryExecution);

        QueryExecutionParam queryExecutionParam = new QueryExecutionParam();
        //Don't set Id - it will be auto generated
        queryExecutionParam.setName("Андрейко");
        queryExecutionParam.setValue("public");
       // queryExecutionParam.setQueryExecution();

        // queryExecution.set
        return queryExecutionParamDao.saveAndFlush(queryExecutionParam);
    }
}
