package org.envtools.monitor.provider.mock.model;

import org.envtools.monitor.model.applications.Application;

/**
 * Created: 11/20/15 11:55 PM
 *
 * @author Yury Yakovlev
 */
public class MockApplication extends Application {
    private String applicationType;
    private String host;
    private int port;
    private String url;
    private String componentName;
    private String version;
    private Double processMemory;

    public String getApplicationType() {
        return applicationType;
    }

    public void setApplicationType(String applicationType) {
        this.applicationType = applicationType;
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

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Double getProcessMemory() {
        return processMemory;
    }

    public void setProcessMemory(Double processMemory) {
        this.processMemory = processMemory;
    }
}
