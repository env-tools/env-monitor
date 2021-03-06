package org.envtools.monitor.model.querylibrary.db;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.envtools.monitor.model.querylibrary.DataProviderType;
import org.envtools.monitor.model.querylibrary.QueryParamType;

import javax.persistence.*;

/**
 * Created: 2/23/16 12:30 AM
 *
 * @author Plotnikova Anastasiya
 */
@Entity
@Table(name = "QUERY_PARAM")
public class QueryParam extends AbstractDbIdentifiable {

    public QueryParam() {
    }

    public QueryParam(String name, QueryParamType type, String defaultValue) {
        this.name = name;
        this.type = type;
        this.defaultValue = defaultValue;
    }

    private String name;

    @ManyToOne
    @JoinColumn(name = "QUERY_ID")
    private LibQuery libQuery;

    @Enumerated(EnumType.STRING)
    private QueryParamType type;

    private String defaultValue;

    public LibQuery getLibQuery() {
        return libQuery;
    }

    public void setLibQuery(LibQuery libQuery) {
        this.libQuery = libQuery;
    }

    public QueryParamType getType() {
        return type;
    }

    public void setType(QueryParamType type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("id", id)
                .append("name", name)
                .append("type", type)
                .append("defaultValue", defaultValue)
                .toString();
    }
}
