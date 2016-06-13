package org.envtools.monitor.module.querylibrary.services.impl.execution;

import edu.umd.cs.findbugs.annotations.Nullable;
import org.apache.log4j.Logger;
import org.envtools.monitor.model.querylibrary.db.QueryExecution;
import org.envtools.monitor.model.querylibrary.execution.*;
import org.envtools.monitor.module.querylibrary.dao.DataSourceDao;
import org.envtools.monitor.module.querylibrary.dao.LibQueryDao;
import org.envtools.monitor.module.querylibrary.dao.QueryExecutionDao;
import org.envtools.monitor.module.querylibrary.dao.QueryExecutionParamDao;
import org.envtools.monitor.module.querylibrary.services.QueryExecutionService;
import org.envtools.monitor.module.querylibrary.services.QueryExecutionTask;
import org.envtools.monitor.module.querylibrary.services.QueryExecutionTaskRegistry;
import org.envtools.monitor.module.querylibrary.services.impl.datasource.JdbcDataSourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.time.LocalDateTime;
import java.util.concurrent.*;

/**
 * Created: 27.02.16 3:42
 *
 * @author Yury Yakovlev
 */
@Service
@Transactional
public class QueryExecutionServiceImpl implements QueryExecutionService {

    private static final Logger LOGGER = Logger.getLogger(QueryExecutionServiceImpl.class);

    @Autowired
    private JdbcDataSourceService jdbcDataSourceService;

    @Autowired
    private QueryExecutionTaskRegistry taskRegistry;

    private ExecutorService threadPool;

    @Autowired
    DataSourceDao dataSourceDao;

    @Autowired
    LibQueryDao libQueryDao;

    @Autowired
    QueryExecutionParamDao queryExecutionParamDao;

    @Autowired
    QueryExecutionDao queryExecutionDao;

    @PostConstruct
    public void init() {
        threadPool = Executors.newCachedThreadPool();
    }

    @PreDestroy
    public void close() {
        threadPool.shutdownNow();
    }

    @Override
    public QueryExecutionResult execute(QueryExecutionRequest queryExecutionRequest) throws QueryExecutionException {

        LOGGER.info("QueryExecutionServiceImpl.execute - Creating task with queryExecutionRequest: "
                + queryExecutionRequest);

        QueryExecutionTask task = createExecutionTask(
                queryExecutionRequest, taskRegistry);

        Future<Object> future = threadPool.submit(Executors.callable(task, null /* no result */));
        taskRegistry.registerTaskFuture(queryExecutionRequest.getOperationId(), future);
        //This future will be used for cancellations only

        Long executionId = saveExecution(queryExecutionRequest);
        if (executionId != null) {
            LOGGER.info("Execution saved : execution id = " + executionId);
        }

        //The task should produce a result not later than in [timeout] ms,
        //or otherwise we get "timeout" result
        QueryExecutionResult result = processSyncQueryTask(task,
                queryExecutionRequest.getTimeOutMs(),
                queryExecutionRequest.getOperationId());

        return result;
    }

    @Override
    public QueryExecutionResult executeNext(QueryExecutionNextResultRequest queryExecutionNextResultRequest) throws QueryExecutionException {
        LOGGER.info("QueryExecutionServiceImpl.executeNext - " +
                "Executing for next result for task with queryExecutionNextResultRequest: "
                + queryExecutionNextResultRequest);
        String operationId = queryExecutionNextResultRequest.getOperationId();
        QueryExecutionTask task = taskRegistry.find(operationId);

        if (task == null) {
            return QueryExecutionResult.ofError(operationId, "Result already closed. Please execute query again.");
        }

        task.postNextResultRequest(queryExecutionNextResultRequest);

        QueryExecutionResult result = processSyncQueryTask(task,
                queryExecutionNextResultRequest.getTimeOutMs(),
                queryExecutionNextResultRequest.getOperationId());

        return result;
    }

    @Override
    public void submitForExecution(QueryExecutionRequest queryExecutionRequest, QueryExecutionListener listener) throws QueryExecutionException {

        LOGGER.info("QueryExecutionServiceImpl.submitForExecution - Creating task with queryExecutionRequest: " + queryExecutionRequest);

        QueryExecutionTask task = createExecutionTask(queryExecutionRequest, taskRegistry);

        task.setLastResultListener(listener);

        Future<Object> future = threadPool.submit(Executors.callable(task, null /* no result */));
        taskRegistry.registerTaskFuture(queryExecutionRequest.getOperationId(), future);
        //This future will be used for cancellations only

        Long executionId = saveExecution(queryExecutionRequest);
        if (executionId != null) {
            LOGGER.info("Execution saved : execution id = " + executionId);
        }

        //TODO support timeout and cancellation

    }

    @Override
    public void submitForNextResult(QueryExecutionNextResultRequest queryExecutionNextResultRequest, QueryExecutionListener listener) throws QueryExecutionException {
        LOGGER.info("QueryExecutionServiceImpl.submitForNextResult - Creating task with queryExecutionNextResultRequest: "
                + queryExecutionNextResultRequest);
        String operationId = queryExecutionNextResultRequest.getOperationId();
        QueryExecutionTask task = taskRegistry.find(operationId);

        if (task == null) {
            listener.onQueryError(new QueryExecutionException("Result already closed. Please execute query again."));
            return;
        }

        task.setLastResultListener(listener);

        task.postNextResultRequest(queryExecutionNextResultRequest);

        //TODO support timeout and cancellation
    }

    private AbstractQueryExecutionTask createExecutionTask(
            QueryExecutionRequest queryExecutionRequest,
            QueryExecutionTaskRegistry taskRegistry) {
        switch (queryExecutionRequest.getQueryType()) {
            case JDBC:
                LOGGER.info("QueryExecutionServiceImpl.createExecutionTask - " +
                        "for request " + queryExecutionRequest);
                return new JdbcQueryExecutionTask(
                        queryExecutionRequest,
                        taskRegistry,
                        jdbcDataSourceService);
            default:
                throw new UnsupportedOperationException(
                        String.format("Query type %s not supported", queryExecutionRequest.getQueryType()));
        }

    }

    @Nullable
    private Long saveExecution(QueryExecutionRequest request) {
       try {
            QueryExecution queryExecution = new QueryExecution();
            queryExecution.setStartTimestamp(LocalDateTime.now());
            queryExecution.setText(request.getQuery());
            queryExecution.setOperationId(request.getOperationId());

           //TODO pass query id and datasource id from request
            //queryExecution.setLibQuery(libQueryDao.getOne(queryExecutionRequest.getLibQuery_id()));
            //queryExecution.setDataSource(dataSourceDao.getOne(queryExecutionRequest.getDataSource_id()));
            return queryExecutionDao.saveAndFlush(queryExecution).getId();

        } catch (Exception e) {
           LOGGER.error("Could not save execution information for request : " + request, e);
           return null;
        }
    }

    @Override
    public void cancel(QueryExecutionCancelRequest cancelRequest) throws QueryExecutionException{
        String operationId = cancelRequest.getOperationId();
        taskRegistry.cancel(operationId);
    }

    private QueryExecutionResult processSyncQueryTask(QueryExecutionTask task,
                                                      Long timeOutMs,
                                                      String operationId) {

        try {
            return task.getLastResult(timeOutMs, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            LOGGER.error("QueryExecutionServiceImpl.processSyncQueryTask - error", e);
            return QueryExecutionResult.ofError(operationId, e);
        } catch (Throwable t) {
            LOGGER.error("QueryExecutionServiceImpl.processSyncQueryTask - unknown error", t);
            return QueryExecutionResult.ofError(operationId, t);
        }
    }

}
