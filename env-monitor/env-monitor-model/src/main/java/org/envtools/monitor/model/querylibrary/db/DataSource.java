package org.envtools.monitor.model.querylibrary.db;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

/**
 * Created: 2/23/16 12:30 AM
 *
 * @author Plotnikova Anastasiya
 */
@Entity
@Table(name = "DATA_SOURCE")
public class DataSource extends AbstractDbIdentifiable {

    public  DataSource() {
    }
    @Column(name = "DATASOURCE_ID")
    private Long id;
    private String type;
    private String name;
    private String description;

    /*dataSource 1 ко многим с таблицей  DataSourceProperties*/
    @OneToMany(mappedBy = "dataSource")
    private List<DataSourceProperties> dataSourciesProperties;

    @ManyToOne
    @JoinColumn(name="QUERYEXECUTION_ID")
    private QueryExecution queryExecution;

    public QueryExecution getQueryExecution() {
        return queryExecution;
    }

    public void setQueryExecution(QueryExecution queryExecution) {
        this.queryExecution = queryExecution;
    }


    public enum getType {
        type
    }


    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).
                append("name", name).
                append("type_code", type).
                append("description", description).
                toString();
    }
}
