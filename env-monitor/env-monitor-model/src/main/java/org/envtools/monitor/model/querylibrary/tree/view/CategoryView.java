package org.envtools.monitor.model.querylibrary.tree.view;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.envtools.monitor.model.querylibrary.db.Category;

import java.util.List;

/**
 * Created: 01.04.16 22:01
 *
 * @author Yury Yakovlev
 */
public class CategoryView {
    private Long id;
    private String title;
    private String description;
    private List<QueryView> queries;
    private List<CategoryView> childCategories;
    private Long parentCategoryId;

    public CategoryView() {
    }

    public CategoryView(Long id, String title, String description, Long parentCategoryId) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.parentCategoryId = parentCategoryId;
    }

    public Long getParentCategory() {
        return parentCategoryId;
    }

    public void setParentCategory(Long parentCategory_id) {
        this.parentCategoryId = parentCategory_id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public List<QueryView> getQueries() {
        return queries;
    }

    public void setQueries(List<QueryView> queries) {
        this.queries = queries;
    }

    public List<CategoryView> getChildCategories() {
        return childCategories;
    }

    public void setChildCategories(List<CategoryView> childCategories) {
        this.childCategories = childCategories;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("id", id)
                .append("title", title)
                .append("description", description)
                .append("queries", queries)
                .append("childCategories", childCategories)
                .append("parentCategoryId", parentCategoryId)
                .toString();
    }
}
