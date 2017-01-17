package org.envtools.monitor.model.querylibrary.db;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.envtools.monitor.model.querylibrary.DataProviderType;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "DATA_SOURCE")
public class DataSource extends AbstractDbIdentifiable {

    public DataSource() {
    }

    @Enumerated(EnumType.STRING)
    @NotNull
    private DataProviderType type;

    @NotEmpty
    private String name;
    private String description;

    /*dataSource 1 ко многим с сущностью DataSourceProperty*/
    @OneToMany(/*mappedBy = "dataSource", */cascade = CascadeType.ALL)
    @JoinColumn(name = "DATASOURCE_ID")
    @OrderBy(value = "property")
    @JsonProperty("properties")
    private List<DataSourceProperty> dataSourceProperties;

    @OneToMany(mappedBy = "dataSource", cascade = CascadeType.ALL)
    private List<QueryExecution> queryExecutions;

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

    public List<DataSourceProperty> getDataSourceProperties() {
        return dataSourceProperties;
    }

    public void setDataSourceProperties(List<DataSourceProperty> dataSourceProperties) {
        this.dataSourceProperties = dataSourceProperties;
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
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("id", id)
                .append("type", type)
                .append("name", name)
                .append("description", description)
                .append("dataSourceProperties", dataSourceProperties)
                .append("queryExecutions", queryExecutions)
                .toString();
    }

    @JsonAnySetter
    public void set(String name, Object value) {
        DataSourceProperty dataSourceProperty = new DataSourceProperty();
        dataSourceProperty.setProperty(name);
        dataSourceProperty.setValue(value.toString());
        if (dataSourceProperties == null) {
            dataSourceProperties = new ArrayList<>();
        }
        dataSourceProperties.add(dataSourceProperty);
    }

}
