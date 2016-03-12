package org.envtools.monitor.model.querylibrary.execution;

import com.google.common.collect.ImmutableMap;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.envtools.monitor.model.querylibrary.QueryType;

import java.util.Map;

/**
 * Created: 27.02.16 3:22
 *
 * @author Yury Yakovlev
 */
public class QueryExecutionRequest {

    // Unique query-execution related id generated by the client
    private final String operationId;

    private final QueryType queryType;
    private final String query;
    private final Map<String, Object> queryParameters;
    private final Map<String, String> dataSourceProperties;
    private final Long timeOutMs;
    private final Integer rowCount;

    public QueryExecutionRequest(
            String operationId,
            QueryType queryType,
            String query,
            Map<String, Object> queryParameters,
            Map<String, String> dataSourceProperties,
            Long timeOutMs,
            Integer rowCount) {
        this.operationId = operationId;
        this.queryType = queryType;
        this.query = query;
        this.queryParameters = queryParameters;
        this.dataSourceProperties = ImmutableMap.copyOf(dataSourceProperties);
        this.timeOutMs = timeOutMs;
        this.rowCount = rowCount;
    }

    public String getOperationId() {
        return operationId;
    }

    public QueryType getQueryType() {
        return queryType;
    }

    public String getQuery() {
        return query;
    }

    public Map<String, Object> getQueryParameters() {
        return queryParameters;
    }

    public Map<String, String> getDataSourceProperties() {
        return dataSourceProperties;
    }

    public Long getTimeOutMs() {
        return timeOutMs;
    }

    public Integer getRowCount() {
        return rowCount;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String operationId;
        private QueryType queryType;
        private String query;
        private Map<String, Object> queryParameters;
        private Map<String, String> dataSourceProperties;
        private Long timeOutMs;
        private Integer rowCount;

        private Builder() {}

        public Builder operationId(String operationId) {
            this.operationId = operationId;
            return this;
        }

        public Builder queryType(QueryType queryType) {
            this.queryType = queryType;
            return this;
        }

        public Builder query(String query) {
            this.query = query;
            return this;
        }

        public Builder queryParameters(Map<String, Object> queryParameters) {
            this.queryParameters = queryParameters;
            return this;
        }

        public Builder dataSourceProperties(Map<String, String> dataSourceProperties) {
            this.dataSourceProperties = dataSourceProperties;
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

        public QueryExecutionRequest build() {
            return new QueryExecutionRequest(
                    operationId,
                    queryType,
                    query,
                    queryParameters,
                    dataSourceProperties,
                    timeOutMs,
                    rowCount
            );
        }
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).
                append("operationId", operationId).
                append("queryType", queryType).
                append("query", query).
                append("queryParameters", queryParameters).
                append("timeOutMs", timeOutMs).
                append("rowCount", rowCount).
                toString();
    }
}
