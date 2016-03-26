package org.envtools.monitor.module.querylibrary.services.impl.execution;

import org.envtools.monitor.model.querylibrary.DataProviderType;
import org.envtools.monitor.model.querylibrary.execution.QueryExecutionRequest;
import org.envtools.monitor.model.querylibrary.execution.QueryExecutionResult;
import org.envtools.monitor.module.querylibrary.QueryExecuteTestApplication;
import org.envtools.monitor.module.querylibrary.services.QueryExecutionService;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import org.springframework.transaction.annotation.Transactional;
import org.junit.Assert;

/**
 * Created by sergey on 23.03.2016.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = QueryExecuteTestApplication.class)
@TestPropertySource(locations = "classpath:/services/application-query-execute-test.properties")
@Transactional
public class QueryExecutionServiceImplTest {

    @Autowired
    private QueryExecutionService executionService;

    @Test
    public void testExecute() throws Exception {
        QueryExecutionRequest.Builder requestBuilder = QueryExecutionRequest.builder();

        String query = "SELECT * FROM INFORMATION_SCHEMA.COLLATIONS WHERE NAME=:name";
        Map<String, Object> queryParameters = new HashMap<>();
        Map<String, String> dataSourceProperties = new HashMap<>();
        long timeOut = 5000;
        int rowCount = 1;
        ;

        queryParameters.put("name", "ARABIC_JORDAN");

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

        Assert.assertEquals(1, result.getResultRows().size());

    }

    @Test
    public void testExecute1() throws Exception {
        QueryExecutionRequest.Builder requestBuilder = QueryExecutionRequest.builder();

        String query = "SELECT * FROM INFORMATION_SCHEMA.CATALOGS WHERE CATALOG_NAME =:name ";
        Map<String, Object> queryParameters = new HashMap<>();
        Map<String, String> dataSourceProperties = new HashMap<>();
        long timeOut = 5000;
        int rowCount = 100;
        ;

        queryParameters.put("name", "UNNAMED");

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
        Assert.assertEquals(1, result.getResultRows().size());
        // Assert.assertEquals(QueryExecutionResult.ExecutionStatusE.TIMED_OUT, result.getStatus().TIMED_OUT);
    }

    @Test
    public void testExecuteSQLException() throws Exception {
        QueryExecutionRequest.Builder requestBuilder = QueryExecutionRequest.builder();

        String query = "FAIL SQL";
        Map<String, String> dataSourceProperties = new HashMap<>();
        long timeOut = 5000;
        int rowCount = 50;


        dataSourceProperties.put("url", "jdbc:h2:mem:;DB_CLOSE_ON_EXIT=FALSE");
        dataSourceProperties.put("user", "sa");
        dataSourceProperties.put("password", "sa");
        dataSourceProperties.put("driverClassName", "org.h2.Driver");


        QueryExecutionRequest request = requestBuilder
                .operationId(UUID.randomUUID().toString())
                .queryType(DataProviderType.JDBC)
                .query(query)
                .dataSourceProperties(dataSourceProperties)
                .timeOutMs(timeOut)
                .rowCount(rowCount)
                .build();

        ExpectedException.none().expect(ExecutionException.class);
        QueryExecutionResult result = executionService.execute(request);
    }
}