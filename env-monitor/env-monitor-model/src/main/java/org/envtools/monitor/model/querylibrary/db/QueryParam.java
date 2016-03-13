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
@Table(name = "QUERY_PARAM")
public class QueryParam extends AbstractDbIdentifiable {

    public QueryParam() {
    }
    @Column(name = "QUERYPARAM_ID")
    private Long id;
    @Column(name = "NAME")
    private String name;
    @Enumerated(EnumType.STRING)
    private DataProviderType type;

    public LibQuery getLibQuery() {
        return libQuery;
    }

    public void setLibQuery(LibQuery libQuery) {
        this.libQuery = libQuery;
    }

    @ManyToOne
    @JoinColumn(name="QUERY_ID")
    private LibQuery libQuery;

    public DataProviderType getType() {
        return type;
    }

    public void setType(DataProviderType type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("name", name)
                .append("type", type)
                .append("libQuery", libQuery)
                .toString();
    }
}
