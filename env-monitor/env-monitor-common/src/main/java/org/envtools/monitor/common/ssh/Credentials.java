package org.envtools.monitor.common.ssh;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by Michal Skuza on 2016-06-17.
 */
@XmlRootElement(name = "credentials")
@XmlAccessorType(value = XmlAccessType.NONE)
public class Credentials {
    private String user;
    private String host;
    private String password;
    private String encryptedPassword;
    private boolean loadAtStartup;

    public Credentials() {
    }

    public Credentials(String user, String host, String password, String encryptedPassword, boolean loadAtStartup) {
        this.user = user;
        this.host = host;
        this.password = password;
        this.encryptedPassword = encryptedPassword;
        this.loadAtStartup = loadAtStartup;
    }

    @XmlElement(name = "user", required = true)
    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    @XmlElement(name = "host", required = true)
    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    @XmlElement(name = "password", required = false)
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @XmlElement(name = "encrypted-password", required = true)
    public String getEncryptedPassword() {
        return encryptedPassword;
    }

    public void setEncryptedPassword(String encryptedPassword) {
        this.encryptedPassword = encryptedPassword;
    }

    @XmlElement(name = "load-at-startup", required = true)
    public boolean isLoadAtStartup() {
        return loadAtStartup;
    }

    public void setLoadAtStartup(boolean loadAtStartup) {
        this.loadAtStartup = loadAtStartup;
    }
}
