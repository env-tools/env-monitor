package org.envtools.monitor.module.querylibrary.services.impl.execution;

import org.apache.log4j.Logger;
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
public class QueryExecutionServiceImpl implements QueryExecutionService{

    private static final Logger LOGGER = Logger.getLogger(QueryExecutionServiceImpl.class);

    @Autowired
    private JdbcDataSourceService jdbcDataSourceService;

    private ExecutorService threadPool;

    @PostConstruct
    public void init() {
        threadPool = Executors.newCachedThreadPool();
    }

    @PreDestroy
    public void close() {
        //TODO close resources ? Ште???
        threadPool.shutdownNow();
    }

    @Override
    public QueryExecutionResult execute(QueryExecutionRequest queryExecutionRequest) throws QueryExecutionException{
        Optional<String> errorMessage = null;
        Optional<Throwable> error = null;
        AbstractQueryExecutionTask task = createExecutionTask(queryExecutionRequest);
        Future<QueryExecutionResult> future = threadPool.submit(task);
        try {
            return future.get(queryExecutionRequest.getTimeOutMs(), TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            //TODO create error result, not null
            LOGGER.error("QueryExecutionServiceImpl.execute - error", e);
            errorMessage= Optional.of(e.toString());
            return new QueryExecutionResult(QueryExecutionResult.ExecutionStatusE.ERROR, 0, 0, null,errorMessage, error);
        } catch (ExecutionException e) {
            //TODO create error result, not null
            LOGGER.error("QueryExecutionServiceImpl.execute - error", e);
            errorMessage= Optional.of(e.toString());
            return new QueryExecutionResult(QueryExecutionResult.ExecutionStatusE.ERROR, 0, 0, null,errorMessage, error);
        } catch (TimeoutException e) {
            //TODO create timeout result, not null
            LOGGER.error("QueryExecutionServiceImpl.execute - timeout", e);
            errorMessage= Optional.of(e.toString());
            return new QueryExecutionResult(QueryExecutionResult.ExecutionStatusE.TIMED_OUT, 0, 0, null,errorMessage,error);
        }
        catch (Throwable t) {
            error = Optional.of(t);
            return new QueryExecutionResult(QueryExecutionResult.ExecutionStatusE.TIMED_OUT, 0, 0, null,errorMessage, error);
        }
    }

    @Override
    public void submitForExecution(QueryExecutionRequest queryExecutionRequest, QueryExecutionListener listener) {
        throw new UnsupportedOperationException("submitForExecution not implemented");
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
