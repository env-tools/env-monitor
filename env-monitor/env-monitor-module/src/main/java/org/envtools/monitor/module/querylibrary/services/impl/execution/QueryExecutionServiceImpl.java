package org.envtools.monitor.module.querylibrary.services.impl.execution;

import com.google.common.util.concurrent.*;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.envtools.monitor.common.util.ExceptionReportingUtil;
import org.envtools.monitor.model.querylibrary.execution.QueryExecutionException;
import org.envtools.monitor.model.querylibrary.execution.QueryExecutionListener;
import org.envtools.monitor.model.querylibrary.execution.QueryExecutionRequest;
import org.envtools.monitor.model.querylibrary.execution.QueryExecutionResult;
import org.envtools.monitor.module.querylibrary.services.QueryExecutionService;
import org.envtools.monitor.module.querylibrary.services.impl.datasource.JdbcDataSourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.Optional;
import java.util.concurrent.*;

/**
 * Created: 27.02.16 3:42
 *
 * @author Yury Yakovlev
 */
@Service
public class QueryExecutionServiceImpl implements QueryExecutionService {

    private static final Logger LOGGER = Logger.getLogger(QueryExecutionServiceImpl.class);

    @Autowired
    private JdbcDataSourceService jdbcDataSourceService;

    private ExecutorService threadPool;
    private ListeningExecutorService threadPoolWithCallbacks;

    @PostConstruct
    public void init() {
        threadPool = Executors.newCachedThreadPool();
        threadPoolWithCallbacks = MoreExecutors.listeningDecorator(threadPool);
    }

    @PreDestroy
    public void close() {
        //TODO close resources ? Ште???
        threadPool.shutdownNow();
    }

    @Override
    public QueryExecutionResult execute(QueryExecutionRequest queryExecutionRequest) throws QueryExecutionException {

        AbstractQueryExecutionTask task = createExecutionTask(queryExecutionRequest);

        Future<QueryExecutionResult> future = threadPool.submit(task);

        try {
            return future.get(queryExecutionRequest.getTimeOutMs(), TimeUnit.MILLISECONDS);
        } catch (InterruptedException | ExecutionException e) {
            LOGGER.error("QueryExecutionServiceImpl.execute - error", e);
            return QueryExecutionResult.ofError(queryExecutionRequest.getOperationId(), e);
        } catch (TimeoutException e) {
            LOGGER.info("QueryExecutionServiceImpl.execute - timeout", e);
            return QueryExecutionResult
                    .builder()
                    .status(QueryExecutionResult.ExecutionStatusE.TIMED_OUT)
                    .error(e)
                    .errorMessage(ExceptionReportingUtil.getExceptionMessage(e))
                    .build();
        } catch (Throwable t) {
            return QueryExecutionResult.ofError(queryExecutionRequest.getOperationId(), t);
        }
    }

    @Override
    public void submitForExecution(QueryExecutionRequest queryExecutionRequest, QueryExecutionListener listener) throws QueryExecutionException{
        AbstractQueryExecutionTask task = createExecutionTask(queryExecutionRequest);
        ListenableFuture<QueryExecutionResult> listenableFuture = threadPoolWithCallbacks.submit(task);

        Futures.addCallback(listenableFuture, new FutureCallback<QueryExecutionResult>() {
            public void onSuccess(QueryExecutionResult result) {
                listener.onQueryCompleted(result);
            }

            public void onFailure(Throwable t) {
                listener.onQueryError(t);
            }
        });

        try {
            listenableFuture.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new QueryExecutionException(e);
            //Don't we report the same twice?
        }

    }

    private AbstractQueryExecutionTask createExecutionTask(QueryExecutionRequest queryExecutionRequest) {
        switch (queryExecutionRequest.getQueryType()) {
            case JDBC:
                LOGGER.info("QueryExecutionServiceImpl.createExecutionTask - " +
                        "for request " + queryExecutionRequest);
                return new JdbcQueryExecutionTask(queryExecutionRequest, jdbcDataSourceService);
            default:
                throw new UnsupportedOperationException(
                        String.format("Query type %s not supported", queryExecutionRequest.getQueryType()));
        }
    }
}
