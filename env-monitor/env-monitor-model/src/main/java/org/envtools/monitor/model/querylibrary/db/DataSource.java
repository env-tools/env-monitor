package org.envtools.monitor.model.querylibrary.db;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import javax.persistence.*;
import java.util.Set;

/**
 * Created: 2/23/16 12:30 AM
 *
 * @author Yury Yakovlev
 */
@Entity
public class DataSource extends AbstractDbIdentifiable {

    public  DataSource() {
    }
    @Column(name = "DATASOURCE_ID")
    private Long id;
    private String type;
    private String name;
    private String description;

    @OneToMany(mappedBy = "dataSource")
    private Set<DataSourceProperties> dataSourceProperties;

    @OneToOne
    @JoinColumn(name = "QUERYEXECUTION_ID")
    private QueryExecution queryExecution;

    public QueryExecution getQueryExecution() {
        return queryExecution;
    }

    public void setQueryExecution(QueryExecution queryExecution) {
        this.queryExecution = queryExecution;
    }

    public String getType() {
        return type;
    }

    public Set<DataSourceProperties> getDataSourceProperties() {
        return dataSourceProperties;
    }

    public void setDataSourceProperties(Set<DataSourceProperties> dataSourceProperties) {
        this.dataSourceProperties = dataSourceProperties;
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
