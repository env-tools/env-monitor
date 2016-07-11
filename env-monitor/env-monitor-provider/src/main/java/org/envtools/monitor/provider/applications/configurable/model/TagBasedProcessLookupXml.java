package org.envtools.monitor.provider.applications.configurable.model;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Michal Skuza on 2016-06-23.
 */
@XmlAccessorType(value = XmlAccessType.PROPERTY)
public class TagBasedProcessLookupXml extends ApplicationLookupXml {
    private List<String> includeTags;
    private List<String> excludeTags;

    public TagBasedProcessLookupXml() {
        this.includeTags = new ArrayList<>();
        this.excludeTags = new ArrayList<>();
    }

    @XmlElementWrapper
    @XmlElement(name = "tag")
    public List<String> getIncludeTags() {
        return includeTags;
    }

    @XmlElementWrapper
    @XmlElement(name = "tag")
    public List<String> getExcludeTags() {
        return excludeTags;
    }

    public void setIncludeTags(List<String> includeTags) {
        this.includeTags = includeTags;
    }

    public void setExcludeTags(List<String> excludeTags) {
        this.excludeTags = excludeTags;
    }

    public void includeTag(String tag) {
        this.includeTags.add(tag);
    }

    public void excludeTag(String tag) {
        this.excludeTags.add(tag);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).
                append("includeTags", includeTags).
                append("excludeTags", excludeTags).
                toString();
    }
}
