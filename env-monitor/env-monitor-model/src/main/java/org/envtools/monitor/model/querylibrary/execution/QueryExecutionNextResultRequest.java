package org.envtools.monitor.model.querylibrary.execution;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * Created: 12.03.16 23:18
 *
 * @author Yury Yakovlev
 */
public class QueryExecutionNextResultRequest extends QueryExecutionRequest {

    private String operationId;
    private Long timeOutMs;
    private Integer rowCount;

    public QueryExecutionNextResultRequest() {
    }

    public QueryExecutionNextResultRequest(String operationId, Long timeOutMs, Integer rowCount) {
        this.operationId = operationId;
        this.timeOutMs = timeOutMs;
        this.rowCount = rowCount;
    }

    public String getOperationId() {
        return operationId;
    }

    public void setOperationId(String operationId) {
        this.operationId = operationId;
    }

    public Long getTimeOutMs() {
        return timeOutMs;
    }

    public void setTimeOutMs(Long timeOutMs) {
        this.timeOutMs = timeOutMs;
    }

    public Integer getRowCount() {
        return rowCount;
    }

    public void setRowCount(Integer rowCount) {
        this.rowCount = rowCount;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).
                append("operationId", operationId).
                append("timeOutMs", timeOutMs).
                append("rowCount", rowCount).
                toString();
    }
}
