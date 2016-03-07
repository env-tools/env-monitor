package org.envtools.monitor.model.querylibrary.db;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import javax.persistence.*;

/**
 * Created: 2/23/16 12:30 AM
 *
 * @author Yury Yakovlev
 */
@Entity
public class DataSourceProperties extends AbstractDbIdentifiable{

    public  DataSourceProperties() {
    }
    @Column(name = "DATASOURCEPROPERTIES_ID")
    private Long id;
    private String property;
    private String value;

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @ManyToOne
    @JoinColumn(name="DATASOURCE_ID")
    private DataSource dataSource;

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).
                append("param", property).
                append("value", value).
                toString();
    }
}
