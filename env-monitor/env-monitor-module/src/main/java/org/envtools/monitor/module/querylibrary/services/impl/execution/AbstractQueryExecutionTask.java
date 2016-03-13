package org.envtools.monitor.module.querylibrary.services.impl.execution;

import org.envtools.monitor.model.querylibrary.execution.QueryExecutionRequest;
import org.envtools.monitor.model.querylibrary.execution.QueryExecutionResult;

import java.util.concurrent.Callable;

/**
 * Created: 13.03.16 1:29
 *
 * @author Yury Yakovlev
 */
public abstract class AbstractQueryExecutionTask implements Callable<QueryExecutionResult> {

    protected final QueryExecutionRequest queryExecutionRequest;

    public AbstractQueryExecutionTask(QueryExecutionRequest queryExecutionRequest) {
       this.queryExecutionRequest = queryExecutionRequest;
    }

    @Override
    public QueryExecutionResult call() throws Exception {
        return doCall();
    }

    protected abstract QueryExecutionResult doCall();
}
