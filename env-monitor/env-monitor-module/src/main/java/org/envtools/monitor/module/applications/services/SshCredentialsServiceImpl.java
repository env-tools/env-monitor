package org.envtools.monitor.module.applications.services;

import org.envtools.monitor.common.jaxb.JaxbHelper;

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
