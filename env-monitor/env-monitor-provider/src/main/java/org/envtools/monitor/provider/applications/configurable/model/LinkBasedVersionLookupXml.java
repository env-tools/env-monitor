package org.envtools.monitor.provider.applications.configurable.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * Created by Michal Skuza on 2016-06-23.
 */

@XmlAccessorType(value = XmlAccessType.PROPERTY)
public class LinkBasedVersionLookupXml extends VersionLookupXml {
    public String link;
    public String linkTargetPattern;

    public LinkBasedVersionLookupXml() {
    }

    public LinkBasedVersionLookupXml(String link, String linkTargetPattern) {
        this.link = link;
        this.linkTargetPattern = linkTargetPattern;
    }

    @XmlElement
    public String getLink() {
        return link;
    }

    @XmlElement
    public String getLinkTargetPattern() {
        return linkTargetPattern;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public void setLinkTargetPattern(String linkTargetPattern) {
        this.linkTargetPattern = linkTargetPattern;
    }
}
