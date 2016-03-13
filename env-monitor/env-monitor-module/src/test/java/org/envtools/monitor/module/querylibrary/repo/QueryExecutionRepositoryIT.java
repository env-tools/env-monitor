package org.envtools.monitor.module.querylibrary.repo;

import org.apache.log4j.Logger;
import org.envtools.monitor.module.querylibrary.PersistenceTestApplication;
import org.junit.runner.RunWith;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created: 10.03.16 22:12
 *
 * @author Anastasiya Plotnikova
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = PersistenceTestApplication.class)
@TestPropertySource(locations="classpath:/persistence/application-persistence-test.properties")

@IntegrationTest
public class QueryExecutionRepositoryIT {
    private static final Logger LOGGER = Logger.getLogger(QueryExecutionRepositoryIT.class);

    public static final String DATASET = "classpath:/persistence/dbunit/lib-query-repo-test.xml";
}
