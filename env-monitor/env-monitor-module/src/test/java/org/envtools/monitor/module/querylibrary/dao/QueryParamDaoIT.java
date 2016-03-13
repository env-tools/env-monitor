package org.envtools.monitor.module.querylibrary.dao;

import org.apache.log4j.Logger;
import org.envtools.monitor.model.querylibrary.db.DataProviderType;
import org.envtools.monitor.model.querylibrary.db.LibQuery;
import org.envtools.monitor.model.querylibrary.db.QueryParam;
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
 * Created: 10.03.16 21:37
 *
 * @author Anastasiya Plotnikova
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = PersistenceTestApplication.class)
@TestPropertySource(locations="classpath:/persistence/application-persistence-test.properties")
@Transactional
public class QueryParamDaoIT {

    private static final Logger LOGGER = Logger.getLogger(LibQueryDaoIT.class);

    @Autowired
    QueryParamDao queryParamDao;
    @Autowired
    LibQueryDao libQueryDao;

    private static final String QUERY_TEXT = "SELECT * FROM LIB_QUERY WHERE title LIKE '%query%'";
    private static final String QUERY_SEARCH_PRESENT = "WHERE";
    private static final String QUERY_SEARCH_ABSENT = "WHAT";

    @Test
    public void testQueryParamContains() {

        Assert.assertTrue(QUERY_TEXT.contains(QUERY_SEARCH_PRESENT));

        createWithText(QUERY_SEARCH_PRESENT);

        List<QueryParam> foundQueries = queryParamDao.getNameQueryParamByText("dfgdfg");
        Assert.assertEquals(1, foundQueries.size());

        List<QueryParam> foundQueries1 = queryParamDao.getNameQueryParamByText("er");
        Assert.assertEquals(0, foundQueries1.size());

        LOGGER.info("Found queries: " + foundQueries);

    }

    @Test
    public void testQueryParamNotContains() {

        Assert.assertFalse(QUERY_TEXT.contains(QUERY_SEARCH_ABSENT));

        createWithText(QUERY_SEARCH_ABSENT);

        List<QueryParam> foundQueries = queryParamDao.getNameQueryParamByText("sdfsdf");
        Assert.assertEquals(0, foundQueries.size());
//
        LOGGER.info("Found queries: " + foundQueries);

    }

    private QueryParam createWithText(String text) {

        LibQuery libQuery1 = new LibQuery();
        //Don't set Id - it will be auto generated
        libQuery1.setText("123");
        libQuery1.setDescription("some_description1");
        libQuery1.setTitle("some_title1");
        libQueryDao.saveAndFlush(libQuery1);


        QueryParam queryParam = new QueryParam();
        //Don't set Id - it will be auto generated
        queryParam.setName("dfgdfg");
        queryParam.setType(DataProviderType.JDBC);
        queryParam.setLibQuery(libQuery1);
        return queryParamDao.saveAndFlush(queryParam);
    }
}
