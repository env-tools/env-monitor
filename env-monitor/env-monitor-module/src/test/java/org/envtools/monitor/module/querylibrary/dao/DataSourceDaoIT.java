package org.envtools.monitor.module.querylibrary.dao;

import org.apache.log4j.Logger;
import org.envtools.monitor.model.querylibrary.db.Category;
import org.envtools.monitor.model.querylibrary.db.DataSource;
import org.envtools.monitor.module.querylibrary.PersistenceTestCategory;
import org.envtools.monitor.module.querylibrary.PersistenceTestDataSource;
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
 * Created by anastasiya on 07.03.16.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes =PersistenceTestDataSource.class)
@TestPropertySource(locations="classpath:/persistence/application-persistence-test.properties")
@Transactional
public class DataSourceDaoIT {

    private static final Logger LOGGER = Logger.getLogger(DataSourceDaoIT.class);

    @Autowired
    DataSourceDao dataSourceDao;

    private static final String NAME = "gjhghjg ZERO";
    private static final String VALUE= "ZERO";
   // private static final String QUERY_SEARCH_ABSENT = "WHAT";

    @Test
    public void testDataSourceContains() {

        Assert.assertTrue(NAME.contains(VALUE));
        createWithText(NAME);
        List<DataSource> foundQueries = dataSourceDao.getNameByText(NAME);
        Assert.assertEquals(1, foundQueries.size());
        Assert.assertEquals(NAME, foundQueries.get(0).getName());

        LOGGER.info("Found queries: " + foundQueries);

    }

    @Test
    public void testDataSourceNotContains() {

        Assert.assertFalse(VALUE.contains(NAME));

        createWithText(VALUE);

        List<DataSource> foundQueries = dataSourceDao.getNameByText(NAME);
        Assert.assertEquals(0, foundQueries.size());

        LOGGER.info("Found queries: " + foundQueries);

    }

    private  DataSource createWithText(String text) {
        DataSource  dataSource = new  DataSource();
        //Don't set Id - it will be auto generated
        dataSource.setDescription("123");
        dataSource.setName(text);
        dataSource.setType("wrwr");

     //   DataSource  dataSource1 = new  DataSource();
        //Don't set Id - it will be auto generated
     //   dataSource1.setDescription("123sfd");
     //   dataSource1.setName("sdfsds222f");
     //   dataSource1.setType("wrwr233323");
       // dataSourceDao.saveAndFlush(dataSource1);

        return  dataSourceDao.saveAndFlush(dataSource);
    }
}
