package org.envtools.monitor.model.querylibrary.tree.view;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * Created by jesa on 02.04.2016.
 */
public class QueryView {
    private String text;
    private String title;
    private String description;
    private Long id;

    public QueryView() {
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("text", text)
                .append("title", title)
                .append("description", description)
                .append("id", id)
                .toString();
    }
}
