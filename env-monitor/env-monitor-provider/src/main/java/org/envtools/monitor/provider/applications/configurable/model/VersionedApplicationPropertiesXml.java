package org.envtools.monitor.provider.applications.configurable.model;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Michal Skuza on 2016-06-23.
 */
@XmlRootElement(name = "applicationsMetadata")
@XmlAccessorType(value = XmlAccessType.FIELD)
public class VersionedApplicationPropertiesXml {
    @XmlElementWrapper(name = "platforms")
    @XmlElement(name = "platform")
    private List<PlatformXml> platforms;

    public VersionedApplicationPropertiesXml() {
        this.platforms = new ArrayList<>();
    }

    public void add(PlatformXml platformXml) {
        this.platforms.add(platformXml);
    }

    public List<PlatformXml> getPlatforms() {
        return platforms;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).
                append("platforms", platforms).
                toString();
    }
}
