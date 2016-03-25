package org.envtools.monitor.model.querylibrary.execution.view;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.io.Serializable;

/**
 * Created: 3/25/16 1:04 AM
 *
 * @author Yury Yakovlev
 */
public class ColumnView implements Serializable {

    private String text;
    private String dataField;

    public ColumnView() {
    }

    public ColumnView(String text, String dataField) {
        this.text = text;
        this.dataField = dataField;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getDataField() {
        return dataField;
    }

    public void setDataField(String dataField) {
        this.dataField = dataField;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).
                append("text", text).
                append("dataField", dataField).
                toString();
    }
}
