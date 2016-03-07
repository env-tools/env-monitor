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
//@Table(name = "LIB_QUERY")
public class QueryParam extends AbstractDbIdentifiable {

    public QueryParam() {
    }
    @Column(name = "QUERYPARAM_ID")
    private Long id;
    private String name;
    private String type;

    @ManyToOne
    @JoinColumn(name="QUERY_ID")
    private Query query;

    public Query getQuery() {
        return query;
    }

    public void setQuery(Query query) {
        this.query = query;
    }

    public String getType() {
        return type;
    }

    public void setType(String datatype) {
        this.type = datatype;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).
                append("name", name).
                append("type", type).
                toString();
    }
}
