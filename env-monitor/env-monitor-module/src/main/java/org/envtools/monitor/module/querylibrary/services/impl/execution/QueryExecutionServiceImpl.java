package org.envtools.monitor.module.querylibrary.services.impl.execution;

import com.google.common.util.concurrent.*;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.envtools.monitor.common.util.ExceptionReportingUtil;
import org.envtools.monitor.model.querylibrary.db.LibQuery;
import org.envtools.monitor.model.querylibrary.db.QueryExecution;
import org.envtools.monitor.model.querylibrary.db.QueryExecutionParam;
import org.envtools.monitor.model.querylibrary.execution.*;
import org.envtools.monitor.module.querylibrary.dao.DataSourceDao;
import org.envtools.monitor.module.querylibrary.dao.LibQueryDao;
import org.envtools.monitor.module.querylibrary.dao.QueryExecutionDao;
import org.envtools.monitor.module.querylibrary.dao.QueryExecutionParamDao;
import org.envtools.monitor.module.querylibrary.services.QueryExecutionService;
import org.envtools.monitor.module.querylibrary.services.impl.datasource.JdbcDataSourceService;
import org.h2.jdbc.JdbcSQLException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
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

    @Autowired
    DataSourceDao dataSourceDao;

    @Autowired
    LibQueryDao libQueryDao;

    @Autowired
    QueryExecutionParamDao queryExecutionParamDao;

    @Autowired
    QueryExecutionDao queryExecutionDao;


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
    public void submitForExecution(QueryExecutionRequest queryExecutionRequest, QueryExecutionListener listener) throws QueryExecutionException {
        LOGGER.info("Create task with queryExecutionRequest: " + queryExecutionRequest);
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

            LocalDateTime localDateTime = LocalDateTime.now();
            QueryExecution queryExecution = new QueryExecution();
            queryExecution.setStartTimestamp(localDateTime);
            queryExecution.setText(queryExecutionRequest.getQuery());
            //queryExecution.setLibQuery(libQueryDao.getOne(queryExecutionRequest.getLibQuery_id()));
            queryExecution.setLibQuery(libQueryDao.getOne((long) 1));
            /* просто для теста, потом надо строку выше раскомментить а эту удалить */
            //queryExecution.setDataSource(dataSourceDao.getOne(queryExecutionRequest.getDataSource_id()));
            queryExecutionDao.saveAndFlush(queryExecution);
            listenableFuture.get();

        } catch (InterruptedException | ExecutionException e) {
            LOGGER.error("QueryExecutionServiceImpl.execute - error", e);

        } catch (Throwable t) {
            QueryExecutionResult.ofError(queryExecutionRequest.getOperationId(), t);
            throw new QueryExecutionException(t);
            //Don't we report the same twice?
        }

    }

    @Override
    public void submitForNextResult(QueryExecutionNextResultRequest queryExecutionNextResultRequest, QueryExecutionListener listener) throws QueryExecutionException {

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

    @Override
    public void cancel(QueryExecutionCancelRequest cancelRequest) {
        //TODO impl cancel
    }
}
