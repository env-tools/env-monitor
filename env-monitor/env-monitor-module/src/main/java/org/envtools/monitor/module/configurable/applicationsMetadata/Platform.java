package org.envtools.monitor.module.configurable.applicationsMetadata;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by MSkuza on 2016-06-23.
 */
@XmlRootElement(name = "platform")
@XmlAccessorType(value = XmlAccessType.FIELD)
public class Platform {
    @XmlAttribute
    private String id;

    @XmlAttribute
    private String name;

    @XmlElementWrapper(name = "environments")
    @XmlElement(name = "environment")
    private List<Environment> environments;

    public Platform() {
        this.environments = new ArrayList<>();
    }

    public Platform(String id, String name) {
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

    public void addEnvironment(Environment environment) {
        this.environments.add(environment);
    }
}
