package org.envtools.monitor.provider.mock.model;

import org.envtools.monitor.model.applications.Application;
import org.envtools.monitor.model.applications.ApplicationStatus;

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

    public MockApplication() {
    }

    public MockApplication(String id, String name, ApplicationStatus status,
                           String applicationType, String host, int port, String url, String componentName, String version, Double processMemory) {
        super(id, name, status);
        this.applicationType = applicationType;
        this.host = host;
        this.port = port;
        this.url = url;
        this.componentName = componentName;
        this.version = version;
        this.processMemory = processMemory;
    }

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

    public static class Builder {
        private String id;
        private String name;
        private ApplicationStatus status;
        private String applicationType;
        private String host;
        private int port;
        private String url;
        private String componentName;
        private String version;
        private Double processMemory;

        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public Builder status(ApplicationStatus status) {
            this.status = status;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }


        public Builder applicationType(String applicationType) {
            this.applicationType = applicationType;
            return this;
        }

        public Builder host(String host) {
            this.host = host;
            return this;
        }

        public Builder port(int port) {
            this.port = port;
            return this;
        }

        public Builder url(String url) {
            this.url = url;
            return this;
        }

        public Builder componentName(String componentName) {
            this.componentName = componentName;
            return this;
        }

        public Builder version(String version) {
            this.version = version;
            return this;
        }

        public Builder processMemory(Double processMemory) {
            this.processMemory = processMemory;
            return this;
        }

        public MockApplication build() {
            return new MockApplication(id, name, status, applicationType, host, port, url, componentName, version, processMemory);
        }
    }
}
