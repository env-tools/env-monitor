package org.envtools.monitor.provider.applications.configurable.model;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Michal Skuza on 2016-06-22.
 */
@XmlRootElement(name = "application")
@XmlAccessorType(value = XmlAccessType.FIELD)
public class VersionedApplicationXml {
    @XmlAttribute
    private String id;

    @XmlElement
    private String name;

    @XmlElement
    private String type;

    @XmlElement
    private String host;

    @XmlElement
    private int port;

    @XmlElement
    private String url;

    @XmlElement(name = "componentname")
    private String componentName;

    @XmlElement
    private MetadataXml metadata;

    @XmlElementWrapper
    @XmlElement(name = "hostee")
    List<VersionedApplicationXml> hostees;

    public VersionedApplicationXml() {
    }

    public VersionedApplicationXml(String id, String name, String type, String host, int port, String url, String componentName) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.host = host;
        this.port = port;
        this.url = url;
        this.componentName = componentName;

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getComponentName() {
        return componentName;
    }

    public void setComponentName(String componentName) {
        this.componentName = componentName;
    }

    public MetadataXml getMetadata() {
        return metadata;
    }

    public void setMetadata(MetadataXml metadataXml) {
        this.metadata = metadataXml;
    }

    public void addHostee(VersionedApplicationXml hostee) {
        if (this.hostees == null) {
            this.hostees = new ArrayList<>();
        }
        this.hostees.add(hostee);
    }

    public List<VersionedApplicationXml> getHostees() {
        return hostees;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).
                append("id", id).
                append("name", name).
                append("type", type).
                append("host", host).
                append("port", port).
                append("url", url).
                append("componentName", componentName).
                append("metadata", metadata).
                append("hostees", hostees).
                toString();
    }
}