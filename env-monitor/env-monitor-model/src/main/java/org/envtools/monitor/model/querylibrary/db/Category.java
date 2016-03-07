package org.envtools.monitor.model.querylibrary.db;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import javax.persistence.*;
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

    private String ower; //пустое для public
    private String title;
    private String description;


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
