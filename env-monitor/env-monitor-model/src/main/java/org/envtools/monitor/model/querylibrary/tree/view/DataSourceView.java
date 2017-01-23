package org.envtools.monitor.model.querylibrary.tree.view;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.util.Map;

/**
 * Created: 15.01.17 4:08
 *
 * @author Yury Yakovlev
 */
public class DataSourceView {

    private String type;
    private String name;
    private String mnemonic;
    private String description;

    private Map<String, String> properties;

    public DataSourceView() {
    }

    public DataSourceView(String type, String name,
                          String mnemonic,
                          String description,
                          Map<String, String> properties) {
        this.type = type;
        this.name = name;
        this.mnemonic = mnemonic;
        this.description = description;
        this.properties = properties;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMnemonic() {
        return mnemonic;
    }

    public void setMnemonic(String mnemonic) {
        this.mnemonic = mnemonic;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Map<String, String> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, String> properties) {
        this.properties = properties;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).
                append("type", type).
                append("name", name).
                append("mnemonic", mnemonic).
                append("description", description).
                append("properties", properties).
                toString();
    }
}
