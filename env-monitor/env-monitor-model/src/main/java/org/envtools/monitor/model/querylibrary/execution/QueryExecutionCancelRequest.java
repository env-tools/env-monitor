package org.envtools.monitor.model.querylibrary.execution;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * Created: 3/24/16 11:54 PM
 *
 * @author Yury Yakovlev
 */
public class QueryExecutionCancelRequest {

    private String operationId;

    public QueryExecutionCancelRequest() {
    }

    public QueryExecutionCancelRequest(String operationId) {
        this.operationId = operationId;
    }

    public String getOperationId() {
        return operationId;
    }

    public void setOperationId(String operationId) {
        this.operationId = operationId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).
                append("operationId", operationId).
                toString();
    }
}
