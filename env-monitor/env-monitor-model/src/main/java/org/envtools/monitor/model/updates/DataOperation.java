package org.envtools.monitor.model.updates;

import org.apache.commons.lang.builder.ToStringBuilder;

import java.util.Map;

/**
 * Created by jesa on 18.03.2016.
 */
public class DataOperation {
    private DataOperationType type; //available types of operations
    private String entity;
    private Map<String, String> fields;

    public DataOperation() {
    }

    public DataOperation(DataOperationType type, String entity, Map<String, String> fields) {
        this.type = type;
        this.entity = entity;
        this.fields = fields;
    }

    public String getEntity() {
        return entity;
    }

    public void setEntity(String entity) {
        this.entity = entity;
    }

    public Map<String, String> getFields() {
        return fields;
    }

    public void setFields(Map<String, String> fields) {
        this.fields = fields;
    }

    public DataOperationType getType() {
        return type;
    }

    public void setType(DataOperationType type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("type", type)
                .append("entity", entity)
                .append("fields", fields)
                .toString();
    }
}
