package org.envtools.monitor.module.querylibrary.services.impl;

import org.envtools.monitor.common.jaxb.JaxbHelper;
import org.envtools.monitor.module.querylibrary.services.Credentials;
import org.envtools.monitor.module.querylibrary.services.SshCredentialsService;

import javax.xml.bind.JAXBException;
import java.io.File;

/**
 * Created by Michal Skuza on 2016-06-17.
 */
public class SshCredentialsServiceImpl implements SshCredentialsService {
    private final String directoryPath;

    public SshCredentialsServiceImpl(String credentialsDirectory) {
        this.directoryPath = credentialsDirectory;
    }

    @Override
    public Credentials getCredentials(String host) {
        try {
            return JaxbHelper.unmarshallFromFile(getCredentialFile(host), Credentials.class, null);
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }
    }

    private File getCredentialFile(String host) {
        return new File(String.format("%s%s.xml", directoryPath, host));
    }
}
