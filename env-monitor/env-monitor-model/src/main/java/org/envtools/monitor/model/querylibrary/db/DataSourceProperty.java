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
@Table(name = "DATA_SOURCE_PROPERTIES")
public class DataSourceProperty extends AbstractDbIdentifiable{

    public DataSourceProperty() {
    }

    private String property;
    private String value;

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
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("id", id)
                .append("property", property)
                .append("value", value)
                .toString();
    }
}
