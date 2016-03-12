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
    private TypeSource type;
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


    public TypeSource getType() {
        return type;
    }

    public void setType(TypeSource type) {
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
        return "DataSource{" +
                "id=" + id +
                ", type=" + type +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", dataSourciesProperties=" + dataSourciesProperties +
                ", queryExecution=" + queryExecution +
                '}';
    }
}
