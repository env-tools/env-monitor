package org.envtools.monitor.module.configurable.applicationsMetadata;

import org.envtools.monitor.module.configurable.VersionedApplication;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by MSkuza on 2016-06-23.
 */
@XmlRootElement(name = "environment")
@XmlAccessorType(value = XmlAccessType.FIELD)
public class Environment {
    @XmlAttribute
    private String id;

    @XmlAttribute
    private String name;

    @XmlElementWrapper
    @XmlElement(name = "application")
    private List<VersionedApplication> applications;

    public Environment() {
        this.applications = new ArrayList<>();
    }

    public Environment(String id, String name) {
        this.id = id;
        this.name = name;
        this.applications = new ArrayList<>();
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

    public void addApplication(VersionedApplication application) {
        this.applications.add(application);
    }
}
