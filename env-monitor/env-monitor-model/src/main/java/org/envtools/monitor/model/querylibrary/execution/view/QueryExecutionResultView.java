package org.envtools.monitor.model.querylibrary.execution.view;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Created: 3/25/16 12:59 AM
 *
 * @author Yury Yakovlev
 */
public class QueryExecutionResultView implements Serializable{

    private List<ColumnView> columns;
    private List<Map<String, String>> result;

    private String status;
    private String message;
    private String details;

    public QueryExecutionResultView() {
    }

    public List<ColumnView> getColumns() {
        return columns;
    }

    public void setColumns(List<ColumnView> columns) {
        this.columns = columns;
    }

    public List<Map<String, String>> getResult() {
        return result;
    }

    public void setResult(List<Map<String, String>> result) {
        this.result = result;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).
                append("columns", columns).
                append("result", result).
                append("status", status).
                append("message", message).
                append("details", details).
                toString();
    }
}
