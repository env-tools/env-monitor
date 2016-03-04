package org.envtools.monitor.module.querylibrary.repo;

import org.apache.log4j.Logger;
import org.envtools.monitor.module.querylibrary.PersistenceTestApplication;
import org.junit.runner.RunWith;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created: 04.03.16 21:31
 *
 * @author Yury Yakovlev
 */

//See http://g00glen00b.be/testing-spring-data-repository/
//for the detailed description

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = PersistenceTestApplication.class)
@TestPropertySource(locations="classpath:/persistence/application-persistence-test.properties")

//@TestExecutionListeners({DependencyInjectionTestExecutionListener.class, DirtiesContextTestExecutionListener.class,
//        TransactionalTestExecutionListener.class, DbUnitTestExecutionListener.class})
//@DatabaseSetup(LibQueryRepositoryIT.DATASET)
//@DatabaseTearDown(type = DatabaseOperation.DELETE_ALL, value = { LibQueryRepositoryIT.DATASET })
//@DirtiesContext
@IntegrationTest
public class LibQueryRepositoryIT {

    private static final Logger LOGGER = Logger.getLogger(LibQueryRepositoryIT.class);

    public static final String DATASET = "classpath:/persistence/dbunit/lib-query-repo-test.xml";

//    @Autowired
//    LibQueryRepository libQueryRepository;

}
