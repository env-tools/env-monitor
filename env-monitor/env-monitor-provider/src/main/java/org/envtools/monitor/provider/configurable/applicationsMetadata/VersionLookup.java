package org.envtools.monitor.provider.configurable.applicationsMetadata;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

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
