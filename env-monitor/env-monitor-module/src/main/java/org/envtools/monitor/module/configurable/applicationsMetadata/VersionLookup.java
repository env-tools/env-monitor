package org.envtools.monitor.module.configurable.applicationsMetadata;

import javax.xml.bind.annotation.*;

/**
 * Created by MSkuza on 2016-06-23.
 */
@XmlRootElement
@XmlAccessorType(value = XmlAccessType.PROPERTY)
public abstract class VersionLookup {
    @XmlElement
    public abstract String getLink();

    @XmlElement
    public abstract String getLinkTargetPattern();
}
