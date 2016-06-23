package org.envtools.monitor.module.configurable.applicationsMetadata;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by MSkuza on 2016-06-23.
 */

public class LinkBasedVersionLookup extends VersionLookup {
    public String link;
    public String linkTargetPattern;

    public LinkBasedVersionLookup() {
    }

    public LinkBasedVersionLookup(String link, String linkTargetPattern) {
        this.link = link;
        this.linkTargetPattern = linkTargetPattern;
    }

    @Override
    public String getLink() {
        return link;
    }

    @Override
    public String getLinkTargetPattern() {
        return linkTargetPattern;
    }
}
