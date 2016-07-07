package org.envtools.monitor.module.configurable;

import org.envtools.monitor.module.configurable.applicationsMetadata.Platform;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Michal Skuza on 2016-06-23.
 */
@XmlRootElement(name = "applicationsMetadata")
@XmlAccessorType(value = XmlAccessType.FIELD)
public class VersionedApplicationProperties {
    @XmlElementWrapper(name = "platforms")
    @XmlElement(name = "platform")
    private List<Platform> platforms;

    public VersionedApplicationProperties() {
        this.platforms = new ArrayList<>();
    }

    public void add(Platform platform) {
        this.platforms.add(platform);
    }

    public List<Platform> getPlatforms() {
        return platforms;
    }
}
