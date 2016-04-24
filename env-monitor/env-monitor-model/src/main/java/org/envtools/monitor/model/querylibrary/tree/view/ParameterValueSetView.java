package org.envtools.monitor.model.querylibrary.tree.view;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by jesa on 22.04.2016.
 */
public class ParameterValueSetView {

    private LocalDateTime timestamp;
    private List<ParameterValueView> parameterValues;

    public ParameterValueSetView() {

    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public List<ParameterValueView> getParameterValues() {
        return parameterValues;
    }

    public void setParameterValues(List<ParameterValueView> parameterValues) {
        this.parameterValues = parameterValues;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("timestamp", timestamp)
                .append("parameterValues", parameterValues)
                .toString();
    }
}
