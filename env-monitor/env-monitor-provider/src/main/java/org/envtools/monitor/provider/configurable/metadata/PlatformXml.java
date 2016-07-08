package org.envtools.monitor.provider.configurable.metadata;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Michal Skuza on 2016-06-23.
 */
@XmlRootElement(name = "platform")
@XmlAccessorType(value = XmlAccessType.FIELD)
public class PlatformXml {
    @XmlAttribute
    private String id;

    @XmlAttribute
    private String name;

    @XmlElementWrapper(name = "environments")
    @XmlElement(name = "environment")
    private List<EnvironmentXml> environments;

    public PlatformXml() {
        this.environments = new ArrayList<>();
    }

    public PlatformXml(String id, String name) {
        this.id = id;
        this.name = name;
        this.environments = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addEnvironment(EnvironmentXml environmentXml) {
        this.environments.add(environmentXml);
    }

    public List<EnvironmentXml> getEnvironments() {
        return environments;
    }
}
