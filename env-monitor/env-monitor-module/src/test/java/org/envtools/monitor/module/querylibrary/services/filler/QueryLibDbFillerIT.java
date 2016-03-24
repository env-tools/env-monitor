package org.envtools.monitor.module.querylibrary.services.filler;

import org.envtools.monitor.model.querylibrary.db.*;
import org.envtools.monitor.module.querylibrary.PersistenceTestApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.testng.Assert;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * Created by sergey on 22.03.2016.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes =PersistenceTestApplication.class)
@TestPropertySource(locations="classpath:/persistence/application-persistence-test.properties")
@Transactional
public class QueryLibDbFillerIT {

    @PersistenceContext
    protected EntityManager em;

    @Test
    public void testFillWithoutHistory() {
        QueryLibDbFiller.fillDatabase(em, false);

        List<Category> categories = em.createQuery("FROM Category").getResultList();
        List<LibQuery> queries = em.createQuery("FROM LibQuery").getResultList();
        List<DataSourceProperty> dataSourceProperties = em.createQuery("FROM DataSourceProperty").getResultList();

        Assert.assertEquals(10, categories.size());
        Assert.assertEquals(10, queries.size());
        Assert.assertEquals(15, dataSourceProperties.size());
    }

    @Test
    public void testFillWithHistory() {
        QueryLibDbFiller.fillDatabase(em, true);

        List<QueryExecution> queryExecutions = em.createQuery("FROM QueryExecution").getResultList();
        List<DataSourceProperty> dataSourceProperties = em.createQuery("FROM DataSourceProperty").getResultList();
        List<QueryExecutionParam> queryExecutionParams = em.createQuery("FROM QueryExecutionParam").getResultList();

        Assert.assertEquals(5, queryExecutions.size());
        Assert.assertEquals(15, dataSourceProperties.size());
        Assert.assertEquals(5, queryExecutionParams.size());
    }
}
