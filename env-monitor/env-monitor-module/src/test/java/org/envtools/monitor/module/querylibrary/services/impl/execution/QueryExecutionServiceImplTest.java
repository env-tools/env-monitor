package org.envtools.monitor.module.querylibrary.services.impl.execution;

import org.envtools.monitor.model.querylibrary.DataProviderType;
import org.envtools.monitor.model.querylibrary.execution.QueryExecutionRequest;
import org.envtools.monitor.model.querylibrary.execution.QueryExecutionResult;
import org.envtools.monitor.module.querylibrary.PersistenceTestApplication;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.testng.Assert;



/**
 * Created by sergey on 23.03.2016.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes =PersistenceTestApplication.class)
@TestPropertySource(locations="classpath:/persistence/application-persistence-test.properties")
public class QueryExecutionServiceImplTest {

    @Test
    public void testExecute() throws Exception {
        QueryExecutionServiceImpl executionService = new QueryExecutionServiceImpl();
        QueryExecutionRequest.Builder requestBuilder = QueryExecutionRequest.builder();

        String query = "SELECT * FROM CATEGORY WHERE ID = :id AND TITLE = :name OR DESCRIPTION = %:desc%";
        Map<String, Object> queryParameters = new HashMap<>();
        Map<String, String> dataSourceProperties = new HashMap<>();
        long timeOut = 300;
        int rowCount = 50;

        queryParameters.put(":id", 10);
        queryParameters.put(":name", "My category");
        queryParameters.put(":desc", "My description");

        dataSourceProperties.put("url", "jdbc:h2:mem:;DB_CLOSE_ON_EXIT=FALSE");
        dataSourceProperties.put("user", "sa");
        dataSourceProperties.put("password", "sa");
        dataSourceProperties.put("driverClassName", "org.h2.Driver");


        QueryExecutionRequest request = requestBuilder
                .operationId(UUID.randomUUID().toString())
                .queryType(DataProviderType.JDBC)
                .query(query)
                .queryParameters(queryParameters)
                .dataSourceProperties(dataSourceProperties)
                .timeOutMs(timeOut)
                .rowCount(rowCount)
                .build();

        QueryExecutionResult result = executionService.execute(request);

    }
}