package org.envtools.monitor.provider.applications.configurable.model;


import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Michal Skuza on 2016-06-23.
 */
@XmlRootElement(name = "environment")
@XmlAccessorType(value = XmlAccessType.FIELD)
public class EnvironmentXml {
    @XmlAttribute
    private String id;

    @XmlAttribute
    private String name;

    @XmlElementWrapper
    @XmlElement(name = "application")
    private List<VersionedApplicationXml> applications;

    public EnvironmentXml() {
        this.applications = new ArrayList<>();
    }

    public EnvironmentXml(String id, String name) {
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

    public void addApplication(VersionedApplicationXml application) {
        this.applications.add(application);
    }

    public List<VersionedApplicationXml> getApplications() {
        return applications;
    }
}
