package org.envtools.monitor.model.querylibrary.db;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import javax.persistence.*;
import java.util.List;

/**
 * Created: 2/23/16 12:30 AM
 *
 * @author Yury Yakovlev
 */
@Entity
@Table(name = "LIB_QUERY")
public class LibQuery extends AbstractDbIdentifiable {

    public LibQuery() {
    }
    @Column(name = "QUERY_ID")
    private Long id;
    private String text;
    private String title;
    private String description;

    @OneToMany(mappedBy = "libQuery")
    private List<QueryParam> queriesParam;

    @OneToMany(mappedBy = "libQuery")
    private List<QueryExecution> queriesExecution;

    public List<QueryParam> getQueriesParam() {
        return queriesParam;
    }

    public void setQueriesParam(List<QueryParam> queriesParam) {
        this.queriesParam = queriesParam;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    @ManyToOne
    @JoinColumn(name="CATEGORY_ID")
     private Category category;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).

                append("text", text).
                append("title", title).
                append("description", description).
                toString();
    }
}
