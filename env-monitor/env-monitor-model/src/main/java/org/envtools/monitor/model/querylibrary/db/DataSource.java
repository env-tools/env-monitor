package org.envtools.monitor.model.querylibrary.db;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import javax.persistence.*;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.List;
import java.util.Set;


@Entity
@Table(name = "DATA_SOURCE")
public class DataSource extends AbstractDbIdentifiable {

    public  DataSource() {
    }
    @Column(name = "DATASOURCE_ID")
    private Long id;
    @Enumerated(EnumType.STRING)
    private DataProviderType type;
    private String name;
    private String description;

    /*dataSource 1 ко многим с таблицей  DataSourceProperties*/
    @OneToMany(mappedBy = "dataSource")
    @OrderBy(value = "PROPERTY")
    private List<DataSourceProperties> dataSourciesProperties;

    @OneToMany(mappedBy = "dataSource")
    @OrderBy(value = "USER")
    private List<QueryExecution> queryExecutions;

   // @ManyToOne
   // @JoinColumn(name="QUERYEXECUTION_ID")
    //private QueryExecution queryExecution;



    public List<QueryExecution> getQueryExecutions() {
        return queryExecutions;
    }

    public void setQueryExecutions(List<QueryExecution> queryExecutions) {
        this.queryExecutions = queryExecutions;
    }

    public DataProviderType getType() {
        return type;
    }

    public void setType(DataProviderType type) {
        this.type = type;
    }

    public List<DataSourceProperties> getDataSourciesProperties() {
        return dataSourciesProperties;
    }

    public void setDataSourciesProperties(List<DataSourceProperties> dataSourciesProperties) {
        this.dataSourciesProperties = dataSourciesProperties;
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
        return new ToStringBuilder(this)
                .append("id", id)
                .append("type", type)
                .append("name", name)
                .append("description", description)
                .append("dataSourciesProperties", dataSourciesProperties)
                .append("queryExecutions", queryExecutions)
                .toString();
    }
}
