package org.envtools.monitor.module.querylibrary.services;

import org.envtools.monitor.model.querylibrary.execution.QueryExecutionListener;
import org.envtools.monitor.model.querylibrary.execution.QueryExecutionNextResultRequest;
import org.envtools.monitor.model.querylibrary.execution.QueryExecutionResult;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

/**
 * Created: 01.05.16 8:31
 *
 * @author Yury Yakovlev
 */
public interface QueryExecutionTask extends Runnable {

    /**
     * Get the result posted by the code of this task
     * This could be partial or full result of the query, or timeout result
     * The method is guaranteed to return not later than in [timeout]
     *
     * @param timeout  How long should we wait until we give up
     * @param unit How long should we wait (unit of time)
     * @return query result
     * @throws InterruptedException
     */
    QueryExecutionResult getLastResult(long timeout, TimeUnit unit)  throws InterruptedException;

    void postNextResultRequest(QueryExecutionNextResultRequest queryExecutionNextResultRequest);

    void setLastResultListener(QueryExecutionListener listener);

    void cancel();

}
