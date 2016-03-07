package org.envtools.monitor.module.querylibrary.dao;

import org.apache.log4j.Logger;
import org.envtools.monitor.model.querylibrary.db.DataSource;
import org.envtools.monitor.model.querylibrary.db.DataSourceProperties;
import org.envtools.monitor.module.querylibrary.PersistenceTestDataSource;
import org.envtools.monitor.module.querylibrary.PersistenceTestDataSourceProperties;
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
 * Created: 07.03.16 21:34
 *
 * @author Anastasiya Plotnikova
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes =PersistenceTestDataSourceProperties.class)
@TestPropertySource(locations="classpath:/persistence/application-persistence-test.properties")
@Transactional
public class DataSourcePropertiesDaoIT {
    private static final Logger LOGGER = Logger.getLogger(DataSourceDaoIT.class);

    @Autowired
    DataSourcePropertiesDao dataSourcePropertiesDao;

    private static final String PARAM = "gjhghjg ZERO";
    private static final String VALUE= "ZERO";
    // private static final String QUERY_SEARCH_ABSENT = "WHAT";

    @Test
    public void testDataSourcePropertiesContains() {

        Assert.assertTrue(PARAM.contains(VALUE));
        createWithText(VALUE);
        List<DataSourceProperties> foundQueries = dataSourcePropertiesDao.getValueByText(VALUE);
        Assert.assertEquals(1, foundQueries.size());

        createWithText(VALUE);
        List<DataSourceProperties> foundQueries1 = dataSourcePropertiesDao.getValueByText(VALUE);
        Assert.assertEquals(2, foundQueries1.size());


        LOGGER.info("Found queries: " + foundQueries);

    }

    @Test
    public void testDataSourcePropertiesNotContains() {

        Assert.assertFalse(VALUE.contains(PARAM));

        createWithText(VALUE);

        List<DataSourceProperties> foundQueries = dataSourcePropertiesDao.getValueByText(PARAM);
        Assert.assertEquals(0, foundQueries.size());

        LOGGER.info("Found queries: " + foundQueries);

    }

    private  DataSourceProperties createWithText(String text) {
        DataSourceProperties dataSourceProperties = new  DataSourceProperties();
        //Don't set Id - it will be auto generated
        dataSourceProperties.setParam("dfgdfg");
        dataSourceProperties.setValue(text);

        return  dataSourcePropertiesDao.saveAndFlush(dataSourceProperties);
    }
}
