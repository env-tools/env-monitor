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
@Table(name = "QUERY")
public class Query extends AbstractDbIdentifiable {

    public Query() {
    }
    @Column(name = "QUERY_ID")
    private Long id;
    private String text;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @ManyToOne
    @JoinColumn(name="CATEGORY_ID")
    private Category category;

    @OneToMany(mappedBy = "query")
    private Set<QueryParam> queryParam;


    @OneToMany(mappedBy = "query") //один ко многим между query и queryExecution
    private Set<QueryExecution> queryExecution;

    public Set<QueryParam> getQueryParam() {
        return queryParam;
    }

    public void setQueryParam(Set<QueryParam> queryParam) {
        this.queryParam = queryParam;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).
                append("text", text).
                toString();
    }
}
