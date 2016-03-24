package org.envtools.monitor.model.querylibrary.db;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Created: 2/23/16 12:30 AM
 *
 * @author Plotnikova Anastasiya
 */
@Entity
@Table(name = "QUERY_EXECUTION")
public class QueryExecution extends AbstractDbIdentifiable  implements Serializable {

    public QueryExecution() {
    }

    private String user;
    private String text;
    private LocalDateTime startTimestamp;
    private LocalDateTime endTimestamp;

    public LocalDateTime getEndTimestamp() {
        return endTimestamp;
    }

    public void setEndTimestamp(LocalDateTime endTimestamp) {
        this.endTimestamp = endTimestamp;
    }

    public LocalDateTime getStartTimestamp() {
        return startTimestamp;
    }

    public void setStartTimestamp(LocalDateTime startTimestamp) {
        this.startTimestamp = startTimestamp;
    }

    public LibQuery getLibQuery() {
        return libQuery;
    }

    public void setLibQuery(LibQuery libQuery) {
        this.libQuery = libQuery;
    }

    public List<QueryExecutionParam> getQueryExecutionParams() {
        return queryExecutionParams;
    }

    public void setQueryExecutionParams(List<QueryExecutionParam> queryExecutionParams) {
        this.queryExecutionParams = queryExecutionParams;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @ManyToOne
    @JoinColumn(name="QUERY_ID")
    private LibQuery libQuery;

    @OneToMany(mappedBy = "queryExecution", cascade = CascadeType.ALL)
    @OrderBy(value = "name")
    private List<QueryExecutionParam> queryExecutionParams;

    @ManyToOne
    @JoinColumn(name="DATASOURCE_ID")
    private DataSource dataSource;

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).
                append("user", user).
                append("text", text).
                append("startTimestamp", startTimestamp).
                append("endTimestamp", endTimestamp).
                append("queryExecutionParams", queryExecutionParams).
                toString();
    }

}
