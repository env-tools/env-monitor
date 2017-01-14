package org.envtools.monitor.module.querylibrary.services.impl.execution;

import com.google.common.collect.Lists;
import org.apache.log4j.Logger;
import org.envtools.monitor.model.querylibrary.DataProviderType;
import org.envtools.monitor.model.querylibrary.execution.*;
import org.envtools.monitor.module.querylibrary.QueryExecuteTestApplication;
import org.envtools.monitor.module.querylibrary.services.QueryExecutionService;

import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.junit.Assert;
import org.unitils.reflectionassert.ReflectionAssert;

/**
 * Created by sergey on 23.03.2016.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = QueryExecuteTestApplication.class)
@TestPropertySource(locations = "classpath:/services/application-query-execute-test.properties")
public class QueryExecutionServiceImplTest {

    private static final Logger LOGGER = Logger.getLogger(QueryExecutionServiceImplTest.class);

    @Autowired
    private QueryExecutionService executionService;

    @Test
    public void testExecuteWithParameters() throws Exception {

        Future<QueryExecutionResult> resultFuture = submitCorrectQuery(
                "SELECT * FROM INFORMATION_SCHEMA.COLLATIONS WHERE NAME=:name",
                new HashMap<String, Object>(){
                    {
                        put("name", "ARABIC_JORDAN");
                    }
                },
                5000,
                50);

        QueryExecutionResult result = resultFuture.get();

        Assert.assertEquals(QueryExecutionResult.ExecutionStatusE.COMPLETED, result.getStatus());
        Assert.assertEquals(1, result.getResultRows().size());

    }

    @Ignore //All queries are too fast in H2 for a timeout =)
    @Test
    public void testExecuteTimedOut() throws Exception {

        QueryExecutionRequest.Builder requestBuilder = QueryExecutionRequest.builder();

        String query = "SELECT m1 || m2 || '---------------------' FROM\n" +
                "(SELECT max(TYPE_NAME) m1 FROM INFORMATION_SCHEMA.TYPE_INFO GROUP BY PRECISION ) t1\n" +
                "LEFT JOIN\n" +
                "(SELECT min(TYPE_NAME) m2 FROM INFORMATION_SCHEMA.TYPE_INFO GROUP BY PRECISION ) t2\n" +
                "ON t1.m1 = t2.m2";
        Map<String, Object> queryParameters = new HashMap<>();
        Map<String, String> dataSourceProperties = new HashMap<>();
        long timeOut = 1;
        int rowCount = 100;

        queryParameters.put("name", "UNNAMED");

        dataSourceProperties.put("url", "jdbc:h2:mem:;DB_CLOSE_ON_EXIT=FALSE");
        dataSourceProperties.put("user", "sa");
        dataSourceProperties.put("password", "sa");
        dataSourceProperties.put("driverClassName", "org.h2.Driver");

        String operationId = UUID.randomUUID().toString();
        QueryExecutionRequest request = requestBuilder
                .operationId(operationId)
                .queryType(DataProviderType.JDBC)
                .query(query)
                .queryParameters(queryParameters)
                .dataSourceProperties(dataSourceProperties)
                .timeOutMs(timeOut)
                .rowCount(rowCount)
                .build();

        CompletableFuture<QueryExecutionResult> resultFuture = new CompletableFuture<>();
        executionService.submitForExecution(request,
                new QueryExecutionListener() {
                    @Override
                    public void onQueryCompleted(QueryExecutionResult queryResult) {
                        resultFuture.complete(queryResult);
                    }

                    @Override
                    public void onQueryTimeout() {
                        resultFuture.complete(QueryExecutionResult.ofTimeout(operationId));
                    }
                });

        Assert.assertEquals(QueryExecutionResult.ExecutionStatusE.TIMED_OUT,
                resultFuture.get().getStatus());
    }

    @Test
    public void testExecuteInvalidSQL() throws Exception {
        QueryExecutionRequest.Builder requestBuilder = QueryExecutionRequest.builder();

        String query = "SELECT FROMd SQL";
        long timeOut = 5000;
        int rowCount = 50;

        String operationId = UUID.randomUUID().toString();
        QueryExecutionRequest request = requestBuilder
                .operationId(operationId)
                .queryType(DataProviderType.JDBC)
                .query(query)
                .dataSourceProperties(getDataSourceProperties())
                .timeOutMs(timeOut)
                .rowCount(rowCount)
                .build();

        CompletableFuture<QueryExecutionResult> future = new CompletableFuture<>();
        executionService.submitForExecution(request,
                new QueryExecutionListener() {
                    @Override
                    public void onQueryError(Throwable t) {
                        future.complete(QueryExecutionResult.ofError(operationId, t));
                    }
                });
        Assert.assertEquals(QueryExecutionResult.ExecutionStatusE.ERROR,
                future.get().getStatus());
    }

    @Test
    public void testSubmitForNextResult() throws Exception {

        //The table is elected to have more rows than ROW_COUNT value
        String query = "SELECT * FROM INFORMATION_SCHEMA.HELP ORDER BY ID";
        int ROW_COUNT = 30;
        long TIMEOUT = 5000;

        // ---------------------------------------------
        //Get first rows by a single query
        // ---------------------------------------------

        Future<QueryExecutionResult> resultFuture = submitCorrectQuery(
                query,
                new HashMap<>(),
                TIMEOUT,
                ROW_COUNT);

        QueryExecutionResult queryExecutionResult = resultFuture.get();

        Assert.assertEquals(QueryExecutionResult.ExecutionStatusE.HAS_MORE_DATA,
                queryExecutionResult.getStatus());
        Assert.assertEquals(ROW_COUNT, queryExecutionResult.getResultRows().size());

        List<Map<String, Object>> expectedRows = queryExecutionResult.getResultRows();

        executionService.cancel(new QueryExecutionCancelRequest(
                queryExecutionResult.getOperationId()));

        sleep(100); //let it cancel =) so that JDBC connection would be released

        // ---------------------------------------------
        //Get first rows by sequential requests for next rows
        // ---------------------------------------------

        int MAX_REQUESTS = 20;
        int currentRequestedRowCount = 1;
        String operationId = null;
        List<Map<String, Object>> partiallyCollectedRows = Lists.newArrayList();
        boolean lastRequest = false;

        for (int iRequest = 0; iRequest < MAX_REQUESTS; iRequest++) {

            LOGGER.info(String.format("#%d * Requesting for %d rows", iRequest, currentRequestedRowCount));

            if (iRequest == 0) {
                //process first request
                resultFuture = submitCorrectQuery(
                        query,
                        new HashMap<>(),
                        TIMEOUT,
                        currentRequestedRowCount);

                queryExecutionResult = resultFuture.get();
                operationId = queryExecutionResult.getOperationId();
                partiallyCollectedRows.addAll(queryExecutionResult.getResultRows());

                Assert.assertEquals(QueryExecutionResult.ExecutionStatusE.HAS_MORE_DATA,
                        queryExecutionResult.getStatus());
                Assert.assertEquals(currentRequestedRowCount, queryExecutionResult.getResultRows().size());

            } else {

                //process following requests - for next rows
                resultFuture = submitForNextResults(
                        operationId,
                        TIMEOUT,
                        currentRequestedRowCount);
                queryExecutionResult = resultFuture.get();
                partiallyCollectedRows.addAll(queryExecutionResult.getResultRows());

                Assert.assertEquals(QueryExecutionResult.ExecutionStatusE.HAS_MORE_DATA,
                        queryExecutionResult.getStatus());
                Assert.assertEquals(currentRequestedRowCount, queryExecutionResult.getResultRows().size());

            }

            LOGGER.info(String.format("#%d * Got %d rows", iRequest, queryExecutionResult.getResultRows().size()));

            if (lastRequest) {
                break;
            }

            currentRequestedRowCount++;

            if (partiallyCollectedRows.size() + currentRequestedRowCount > ROW_COUNT) {
                currentRequestedRowCount = ROW_COUNT - partiallyCollectedRows.size();
                if (currentRequestedRowCount == 0) {
                    break;
                } else {
                    lastRequest = true;
                }
            }
        }

        ReflectionAssert.assertReflectionEquals(expectedRows, partiallyCollectedRows);

    }

    private Map<String, String> getDataSourceProperties() {
        Map<String, String> dataSourceProperties = new HashMap<>();
        dataSourceProperties.put("url", "jdbc:h2:mem:;DB_CLOSE_ON_EXIT=FALSE");
        dataSourceProperties.put("user", "sa");
        dataSourceProperties.put("password", "sa");
        dataSourceProperties.put("driverClassName", "org.h2.Driver");
        return dataSourceProperties;
    }

    private CompletableFuture<QueryExecutionResult> submitCorrectQuery(
            String query,
            Map<String, Object> queryParameters,
            long timeOut,
            int rowCount) {

        QueryExecutionRequest request = QueryExecutionRequest
                .builder()
                .operationId(UUID.randomUUID().toString())
                .queryType(DataProviderType.JDBC)
                .query(query)
                .queryParameters(queryParameters)
                .dataSourceProperties(getDataSourceProperties())
                .timeOutMs(timeOut)
                .rowCount(rowCount)
                .build();

        CompletableFuture<QueryExecutionResult> future = new CompletableFuture<>();
        executionService.submitForExecution(request,
                new QueryExecutionListener() {
                    @Override
                    public void onQueryCompleted(QueryExecutionResult queryResult) {
                        future.complete(queryResult);
                    }
                });

        return future;
    }

    private CompletableFuture<QueryExecutionResult> submitForNextResults(
            String operationId,
            long timeOut,
            int rowCount) {

        QueryExecutionNextResultRequest request = QueryExecutionNextResultRequest
                .builder()
                .operationId(operationId)
                .timeOutMs(timeOut)
                .rowCount(rowCount)
                .build();

        CompletableFuture<QueryExecutionResult> future = new CompletableFuture<>();
        executionService.submitForNextResult(request,
                new QueryExecutionListener() {
                    @Override
                    public void onQueryCompleted(QueryExecutionResult queryResult) {
                        future.complete(queryResult);
                    }
                });

        return future;
    }

    private static void sleep(long ms) {
        try {
            TimeUnit.MILLISECONDS.sleep(ms);
        } catch (InterruptedException e) {
            LOGGER.error("Error on sleep", e);
        }
    }
}