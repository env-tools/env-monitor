package org.envtools.monitor.model.querylibrary.db;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import javax.persistence.*;
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
public class QueryExecution extends AbstractDbIdentifiable {

    public QueryExecution() {
    }
    @Column(name = "QUERYEXECUTION_ID")
    private Long id;
    private String user;
    private LocalTime startTimestamp;

    public LocalTime getEndTimestamp() {
        return endTimestamp;
    }

    public void setEndTimestamp(LocalTime endTimestamp) {
        this.endTimestamp = endTimestamp;
    }

    public LocalTime getStartTimestamp() {
        return startTimestamp;
    }

    public void setStartTimestamp(LocalTime startTimestamp) {
        this.startTimestamp = startTimestamp;
    }

    private LocalTime endTimestamp;

    public LibQuery getLibQuery() {
        return libQuery;
    }

    public void setLibQuery(LibQuery libQuery) {
        this.libQuery = libQuery;
    }



    public List<QueryExecutionParam> getQueriesExecutionParam() {
        return queriesExecutionParam;
    }

    public void setQueriesExecutionParam(List<QueryExecutionParam> queriesExecutionParam) {
        this.queriesExecutionParam = queriesExecutionParam;
    }

    @ManyToOne
    @JoinColumn(name="QUERY_ID")

    private LibQuery libQuery;

    @OneToMany(mappedBy = "queryExecution")
    private List<QueryExecutionParam> queriesExecutionParam;

    @OneToMany(mappedBy = "queryExecution")
    private List<DataSource> DataSourcies;

    public List<DataSource> getDataSourcies() {
        return DataSourcies;
    }

    public void setDataSourcies(List<DataSource> dataSourcies) {
        DataSourcies = dataSourcies;
    }



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
                append("startTimestamp",  startTimestamp.toString()).
                        append("endTimestamp",  endTimestamp.toString()).
                toString();
    }
}
