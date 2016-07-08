package org.envtools.monitor.provider.configurable.metadata;

import javax.xml.bind.annotation.*;

/**
 * Created by Michal Skuza on 2016-06-23.
 */
@XmlRootElement(name = "metadata")
@XmlAccessorType(value = XmlAccessType.FIELD)
public class Metadata {
    @XmlElements({@XmlElement(name = "tagBasedProcessLookup", type = TagBasedProcessLookup.class)})
    private ApplicationLookup applicationLookup;


    @XmlElements({@XmlElement(name = "linkBasedVersionLookup", type = LinkBasedVersionLookup.class)})
    private VersionLookup versionLookup;

    public Metadata() {
    }

    public Metadata(ApplicationLookup applicationLookup) {
        this.applicationLookup = applicationLookup;
    }

    public ApplicationLookup getApplicationLookup() {
        return applicationLookup;
    }

    public void setApplicationLookup(ApplicationLookup applicationLookup) {
        this.applicationLookup = applicationLookup;
    }

    public VersionLookup getVersionLookup() {
        return versionLookup;
    }

    public void setVersionLookup(VersionLookup versionLookup) {
        this.versionLookup = versionLookup;
    }
}
