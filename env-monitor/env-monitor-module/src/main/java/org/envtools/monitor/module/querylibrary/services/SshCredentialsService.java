package org.envtools.monitor.module.querylibrary.services;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by Michal Skuza on 2016-06-17.
 */
public interface SshCredentialsService {
    Credentials getCredentials(String host);
}
