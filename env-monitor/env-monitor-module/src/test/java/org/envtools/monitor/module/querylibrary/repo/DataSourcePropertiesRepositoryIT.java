package org.envtools.monitor.module.querylibrary.repo;

import org.apache.log4j.Logger;
import org.envtools.monitor.module.querylibrary.PersistenceTestApplication;
import org.junit.runner.RunWith;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created: 07.03.16 21:35
 *
 * @author Anastasiya Plotnikova
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = PersistenceTestApplication.class)
@TestPropertySource(locations="classpath:/persistence/application-persistence-test.properties")


@IntegrationTest
public class DataSourcePropertiesRepositoryIT {
    private static final Logger LOGGER = Logger.getLogger(DataSourcePropertiesRepositoryIT.class);

    public static final String DATASET = "classpath:/persistence/dbunit/data-source-properties-repo-test.xml";
}
