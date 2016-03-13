package org.envtools.monitor.model.querylibrary.db;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import javax.persistence.*;

/**
 * Created: 2/23/16 12:30 AM
 *
 * @author Plotnikova Anastasiya
 */
@Entity
@Table(name = "QUERY_EXECUTION_PARAM")
public class QueryExecutionParam  extends AbstractDbIdentifiable{

    public QueryExecutionParam() {
    }
    @Column(name = "QUERYEXECUTIONPARAM_ID")
    private Long id;
    @Column(name="NAME")
    private String name;
    private String value;

    @ManyToOne
    @JoinColumn(name="QUERYEXECUTION_ID")
    private QueryExecution queryExecution;

    public QueryExecution getQueryExecution() {
        return queryExecution;
    }

    public void setQueryExecution(QueryExecution queryExecution) {
        this.queryExecution = queryExecution;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("name", name)
                .append("value", value)
                .append("queryExecution", queryExecution)
                .toString();
    }
}
