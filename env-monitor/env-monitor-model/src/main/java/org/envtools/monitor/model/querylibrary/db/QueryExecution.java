package org.envtools.monitor.model.querylibrary.db;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;

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
    @Column(name = "QUERYEXECUTION_ID")
    private Long id;
    @Column(name = "USER")
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


    public List<QueryExecutionParam> getQueriesExecutionParams() {
        return queriesExecutionParams;
    }

    public void setQueriesExecutionParams(List<QueryExecutionParam> queriesExecutionParams) {
        this.queriesExecutionParams = queriesExecutionParams;
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

    @OneToMany(mappedBy = "queryExecution")
    @OrderBy(value = "NAME")
    private List<QueryExecutionParam> queriesExecutionParams;

   // @OneToMany(mappedBy = "queryExecution")
   // private List<DataSource> DataSourcies;

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
        return "QueryExecution{" +
                "id=" + id +
                ", user='" + user + '\'' +
                ", text='" + text + '\'' +
                ", startTimestamp=" + startTimestamp +
                ", endTimestamp=" + endTimestamp +
                ", libQuery=" + libQuery +
                ", queriesExecutionParams=" + queriesExecutionParams +
                '}';
    }
}
