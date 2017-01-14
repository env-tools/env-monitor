package org.envtools.monitor.model.querylibrary.execution;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.envtools.monitor.model.querylibrary.DataProviderType;

import java.util.Map;

/**
 * Created: 12.03.16 23:18
 *
 * @author Yury Yakovlev
 */
public class QueryExecutionNextResultRequest {

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

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String operationId;
        private Long timeOutMs;
        private Integer rowCount;

        private Builder() {
        }

        public Builder operationId(String operationId) {
            this.operationId = operationId;
            return this;
        }

        public Builder timeOutMs(Long timeOutMs) {
            this.timeOutMs = timeOutMs;
            return this;
        }

        public Builder rowCount(Integer rowCount) {
            this.rowCount = rowCount;
            return this;
        }

        public QueryExecutionNextResultRequest build() {
            return new QueryExecutionNextResultRequest(
                    operationId,
                    timeOutMs,
                    rowCount
            );
        }

    }
}
