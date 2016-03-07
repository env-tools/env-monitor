package org.envtools.monitor.model.querylibrary.db;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import javax.persistence.*;
import java.util.Set;
/* Category --- Query OneToMany*/
/**
 * Created: 2/23/16 12:30 AM
 *
 * @author Yury Yakovlev
 */
@Entity
@Table(name = "CATEGORY")
public class Category extends AbstractDbIdentifiable {

    public Category() {
    }
    @Column(name = "CATEGORY_ID")
    private Long id;
    private String ower; //пустое для public
    private String title;
    private String description;
    @OneToMany(mappedBy = "category")
    private Set<Query> query;

    /*Один ко многим к одной таблице*/
    @OneToMany(mappedBy = "category1")
    private Set<Category> categoryRelation;

    @ManyToOne
   // @JoinColumn(name="CATEGORY_ID")
    private Category category1;

    public Set<Query> getQuery() {
        return query;
    }

    public void setQuery(Set<Query> query) {
        this.query = query;
    }

    public String getOwer() {
        return ower;
    }

    public void setOwer(String ower) {
        this.ower = ower;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).
                append("ower", ower).
                append("title", title).
                append("parent_category", description).
                toString();
    }
}
