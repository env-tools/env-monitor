package org.envtools.monitor.model.querylibrary.db;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import javax.persistence.*;
import java.util.ArrayList;
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

    public LibQuery(String text, String title, String description, List<QueryParam> queryParams, List<QueryExecution> queryExecutions, Category category) {
        this.text = text;
        this.title = title;
        this.description = description;
        this.queryParams = queryParams;
        this.queryExecutions = queryExecutions;
        this.category = category;
    }

    @Column(length = 65536)
    private String text;
    private String title;
    private String description;

    @OneToMany(mappedBy = "libQuery", cascade = CascadeType.ALL)
    @OrderBy(value = "name")
    private List<QueryParam> queryParams = new ArrayList<>();

    @OneToMany(mappedBy = "libQuery", cascade = CascadeType.ALL)
    private List<QueryExecution> queryExecutions;

    @ManyToOne
    @JoinColumn(name = "CATEGORY_ID")
    private Category category;

    @ManyToMany
    @JoinTable(name = "LIB_QUERY_RELATED_DS",
            joinColumns = {
                    @JoinColumn(name="LIB_QUERY_ID")
            },
            inverseJoinColumns = {
                    @JoinColumn(name="DATA_SOURCE_ID")
            }

    )
    @OrderColumn(name = "DATA_SOURCE_INDEX")
    private List<DataSource> relatedDataSources = new ArrayList<>();

    public List<QueryParam> getQueryParams() {
        return queryParams;
    }

    public void addQueryParam(QueryParam queryParam) {
        if (queryParam == null) {
            throw new IllegalArgumentException("QueryParam shouldn't be null");
        }
        this.queryParams.add(queryParam);
        queryParam.setLibQuery(this);
    }

    public void addRelatedDataSource(DataSource dataSource) {
        if (dataSource == null) {
            throw new IllegalArgumentException("Related data source cannot be null");
        }
        this.relatedDataSources.add(dataSource);
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        if (category == null) {
            throw new IllegalArgumentException("Category shouldn't be null");
        }
        this.category = category;
        category.getQueries().add(this);
    }

    public List<QueryExecution> getQueryExecutions() {
        return queryExecutions;
    }

    public void setQueryExecutions(List<QueryExecution> queryExecutions) {
        this.queryExecutions = queryExecutions;
    }

    public List<DataSource> getRelatedDataSources() {
        return relatedDataSources;
    }

    public void setRelatedDataSources(List<DataSource> relatedDataSources) {
        this.relatedDataSources = relatedDataSources;
    }

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
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("id", id)
                .append("text", text)
                .append("title", title)
                .append("description", description)
                .append("queryParams", queryParams)
                .append("queryExecutions", queryExecutions)
                .append("relatedDataSources", relatedDataSources)
                .toString();
    }
}
