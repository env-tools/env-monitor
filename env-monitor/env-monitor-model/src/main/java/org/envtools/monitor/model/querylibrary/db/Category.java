package org.envtools.monitor.model.querylibrary.db;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import javax.persistence.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
/* Category --- Query OneToMany*/
/**
 * Created: 2/23/16 12:30 AM
 *
 * @author Plotnikova Anastasiya
 */
@Entity
@Table(name = "CATEGORY")
public class Category extends AbstractDbIdentifiable {

    public Category() {
    }
   // @Column(name = "CATEGORY_ID")
   // private Long id;
    private String owner; //пустое для public
    private String title;
    private String description;
     /*Один ко многим к таблице LibQuery*/
    @OneToMany(mappedBy = "category")
    private List<LibQuery> queries;



    /*Один ко многим к одной таблице*/
    @OneToMany(mappedBy = "category1")
    private List<Category> categories;

    @ManyToOne
    // @JoinColumn(name="CATEGORY_ID")
    private Category category1;

    public List<Category> getChildCategories() {
        return categories;
    }


   // public void setChildCategories(List<Category> categories) {
   //     this.categories = categories;
  //  }

    public Category getParentCategory() {
        return category1;
    }

    public void setCategory1(Category category1) {
        this.category1 = category1;
    }



  //  public Set<Query> getQuery() {
  //      return query;
 //   }

 //   public void setQuery(Set<Query> query) {
 //       this.query = query;
 //   }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
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
                append("owner", owner).
                append("title", title).
                append("parent_category", description).
                toString();
    }
}
