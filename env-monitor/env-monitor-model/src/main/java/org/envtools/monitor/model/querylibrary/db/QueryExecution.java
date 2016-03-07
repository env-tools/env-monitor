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
public class QueryExecution {

    public QueryExecution() {
    }


    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;
    private String user;
    private Long timestanp;
   // private String description;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTimestanp() {
        return timestanp;
    }

    public void setTimestanp(Long timestanp) {
        this.timestanp = timestanp;
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
                append("id", id).
                append("user", user).
                append("timestanp", timestanp.toString()).
                toString();
    }
}
