package org.envtools.monitor.module.querylibrary.services.impl.execution;

import com.google.common.collect.Queues;
import org.envtools.monitor.model.querylibrary.execution.QueryExecutionListener;
import org.envtools.monitor.model.querylibrary.execution.QueryExecutionNextResultRequest;
import org.envtools.monitor.model.querylibrary.execution.QueryExecutionRequest;
import org.envtools.monitor.model.querylibrary.execution.QueryExecutionResult;
import org.envtools.monitor.module.querylibrary.services.QueryExecutionTask;
import org.envtools.monitor.module.querylibrary.services.QueryExecutionTaskRegistry;

import java.time.LocalDateTime;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * Created: 13.03.16 1:29
 *
 * @author Yury Yakovlev
 */
public abstract class AbstractQueryExecutionTask implements QueryExecutionTask {

    private static final int QUEUE_CAPACITY = 100;

    protected LocalDateTime creationTime = LocalDateTime.now();
    protected volatile boolean isCancelled = false;

    protected volatile QueryExecutionListener listener;

    protected final QueryExecutionRequest queryExecutionRequest;
    protected final QueryExecutionTaskRegistry taskRegistry;

    protected final BlockingQueue<QueryExecutionNextResultRequest> nextResultRequests =
            Queues.newArrayBlockingQueue(QUEUE_CAPACITY);
    protected final BlockingQueue<QueryExecutionResult> results =
            Queues.newArrayBlockingQueue(QUEUE_CAPACITY);

    public AbstractQueryExecutionTask(QueryExecutionRequest queryExecutionRequest,
                                      QueryExecutionTaskRegistry taskRegistry) {
        this.queryExecutionRequest = queryExecutionRequest;
        this.taskRegistry = taskRegistry;
    }

    @Override
    public void run() {

        if (isCancelled) {
            postResult(
                    QueryExecutionResult.ofCancel(
                            queryExecutionRequest.getOperationId()));
            return;
        }

        try {
            taskRegistry.registerTask(queryExecutionRequest.getOperationId(), this);
            doRun();
        } catch (Throwable t) {
            postResult(QueryExecutionResult.ofError(
                    getOperationId(), t));
        } finally {
            taskRegistry.remove(queryExecutionRequest.getOperationId(), this);
        }
    }

    protected void postResult(QueryExecutionResult result) {
        results.offer(result);
        //Protect against concurrent listener updates
        QueryExecutionListener currentListener = listener;

        if (currentListener != null) {
            switch (result.getStatus()) {
                case COMPLETED:
                case HAS_MORE_DATA:
                    currentListener.onQueryCompleted(result);
                    break;
                case CANCELLED:
                    currentListener.onQueryCancelled();
                    break;
                case TIMED_OUT:
                    currentListener.onQueryTimeout();
                    break;
                default:
                    currentListener.onQueryError(result.getError().orElseGet(null));
            }
        }
    }

    @Override
    public QueryExecutionResult getLastResult(long timeout, TimeUnit unit) throws InterruptedException {
        QueryExecutionResult result =  results.poll(timeout, unit);
        if (result == null) {
            return QueryExecutionResult.ofTimeout(queryExecutionRequest.getOperationId());
        }

        return result;
    }

    @Override
    public void postNextResultRequest(QueryExecutionNextResultRequest queryExecutionNextResultRequest) {
        nextResultRequests.offer(queryExecutionNextResultRequest);
    }

    @Override
    public void setLastResultListener(QueryExecutionListener listener) {
        this.listener = listener;
    }

    /**
     * Expected data operation exceptions are encoded in the result (and not thrown)
     *
     * @return Execution result (partial or final)
     */
    protected abstract void doRun();

    public void cancel(){
        isCancelled = true;
    }

    protected boolean isCancelled() {
        return isCancelled;
    }

    protected String getOperationId() {
        return queryExecutionRequest.getOperationId();
    }
}
