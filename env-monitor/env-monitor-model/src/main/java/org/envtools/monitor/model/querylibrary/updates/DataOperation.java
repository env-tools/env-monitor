package org.envtools.monitor.model.querylibrary.updates;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.springframework.stereotype.Repository;

import java.util.Map;

/**
 * Created by jesa on 18.03.2016.
 */
@Repository
public class DataOperation {
    private DataOperationType type; //available types of operations
    private String entity;
    private Map<String, String> fields;
    private Long id;

    public DataOperation() {
    }

    public DataOperation(DataOperationType type, String entity, Map<String, String> fields, Long id) {
        this.type = type;
        this.entity = entity;
        this.fields = fields;
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
                .append("id", id)
                .toString();
    }
}
