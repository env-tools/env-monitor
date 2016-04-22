package org.envtools.monitor.model.querylibrary.tree.view;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.util.List;

/**
 * Created by jesa on 02.04.2016.
 */
public class QueryView {
    private String text;
    private String title;
    private String description;
    private Long id;
    private List<ParameterView> parameters;
    private List<ParameterValueSetView> parameterHistory;

    public QueryView() {
    }

    public QueryView(String text, String title, String description, Long id, List<ParameterView> parameters, List<ParameterValueSetView> parameterHistory) {
        this.text = text;
        this.title = title;
        this.description = description;
        this.id = id;
        this.parameters = parameters;
        this.parameterHistory = parameterHistory;
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

    public List<ParameterView> getParameters() {
        return parameters;
    }

    public void setParameters(List<ParameterView> parameters) {
        this.parameters = parameters;
    }

    public List<ParameterValueSetView> getParameterHistory() {
        return parameterHistory;
    }

    public void setParameterHistory(List<ParameterValueSetView> parameterHistory) {
        this.parameterHistory = parameterHistory;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("text", text)
                .append("title", title)
                .append("description", description)
                .append("id", id)
                .append("parameters", parameters)
                .append("parameterHistory", parameterHistory)
                .toString();
    }
}
