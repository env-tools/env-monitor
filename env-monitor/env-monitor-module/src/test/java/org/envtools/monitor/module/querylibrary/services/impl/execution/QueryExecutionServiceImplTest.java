package org.envtools.monitor.module.querylibrary.services.impl.execution;

import org.envtools.monitor.model.querylibrary.DataProviderType;
import org.envtools.monitor.model.querylibrary.execution.QueryExecutionListener;
import org.envtools.monitor.model.querylibrary.execution.QueryExecutionRequest;
import org.envtools.monitor.model.querylibrary.execution.QueryExecutionResult;
import org.envtools.monitor.module.querylibrary.QueryExecuteTestApplication;
import org.envtools.monitor.module.querylibrary.services.QueryExecutionService;

import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

import org.junit.Assert;
import reactor.event.dispatch.SynchronousDispatcher;

import static org.mockito.Mockito.doAnswer;

/**
 * Created by sergey on 23.03.2016.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = QueryExecuteTestApplication.class)
@TestPropertySource(locations = "classpath:/services/application-query-execute-test.properties")
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
        int rowCount = 50;

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
        QueryExecutionListener listener = null;
        CompletableFuture<QueryExecutionResult> future = new CompletableFuture<>();
        executionService.submitForExecution(request,
                new QueryExecutionListener() {
                    @Override
                    public void onQueryCompleted(QueryExecutionResult queryResult) {
                        //  listener.onQueryCompleted(queryResult);
                        future.complete(queryResult);
                    }

                    @Override
                    public void onQueryError(Throwable t) {
                        //  listener.onQueryError(t);
                    }
                });
        //   QueryExecutionResult result = executionService.execute(request);

        Assert.assertEquals(1, future.get().getResultRows().size());

    }

    // Class under test
    private QueryExecutionServiceImpl queryExequtiomServiceImpl;

    @Mock
    private QueryExecutionServiceImpl qs;


    @Test
    public void testExecuteTimedOut() throws Exception {

        QueryExecutionRequest.Builder requestBuilder = QueryExecutionRequest.builder();

        String query = "SELECT * FROM INFORMATION_SCHEMA.CATALOGS WHERE CATALOG_NAME =:name ";
        Map<String, Object> queryParameters = new HashMap<>();
        Map<String, String> dataSourceProperties = new HashMap<>();
        long timeOut = 10;
        int rowCount = 100;

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
        QueryExecutionListener listener = null;
        Executor executor = new SynchronousDispatcher();
        CompletableFuture<QueryExecutionResult> future = new CompletableFuture<>();
        executionService.submitForExecution(request,
                new QueryExecutionListener() {
                    @Override
                    public void onQueryCompleted(QueryExecutionResult queryResult) {
                        //  listener.onQueryCompleted(queryResult);
                        future.complete(queryResult);
                    }

                    @Override
                    public void onQueryError(Throwable t) {
                        //  listener.onQueryError(t);
                    }
                });

        // QueryExecutionResult result = executionService.execute(request);
        Assert.assertEquals(QueryExecutionResult.ExecutionStatusE.TIMED_OUT, future.get().getStatus().TIMED_OUT);
    }

    @Test
    public void testExecuteFailSQL() throws Exception {
        QueryExecutionRequest.Builder requestBuilder = QueryExecutionRequest.builder();

        String query = "SELECT FROMd SQL";
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
        QueryExecutionListener listener = null;
        Executor executor = new SynchronousDispatcher();
        CompletableFuture<QueryExecutionResult> future = new CompletableFuture<>();
        executionService.submitForExecution(request,
                new QueryExecutionListener() {
                    @Override
                    public void onQueryCompleted(QueryExecutionResult queryResult) {
                        //  listener.onQueryCompleted(queryResult);
                        future.complete(queryResult);
                    }

                    @Override
                    public void onQueryError(Throwable t) {
                      //  listener.onQueryError(t);
                        // future.complete(t);
                    }
                });
        //  QueryExecutionResult result = executionService.execute(request);
          Assert.assertEquals(QueryExecutionResult.ExecutionStatusE.ERROR, future.get().getStatus().ERROR);
    }
}