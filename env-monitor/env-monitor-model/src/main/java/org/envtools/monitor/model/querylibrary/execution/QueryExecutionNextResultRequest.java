package org.envtools.monitor.model.querylibrary.execution;

/**
 * Created: 12.03.16 23:18
 *
 * @author Yury Yakovlev
 */
public class QueryExecutionNextResultRequest {

    private final String operationId;
    private final Long timeOutMs;
    private final Integer rowCount;

    public QueryExecutionNextResultRequest(String operationId, Long timeOutMs, Integer rowCount) {
        this.operationId = operationId;
        this.timeOutMs = timeOutMs;
        this.rowCount = rowCount;
    }
}
