package org.envtools.monitor.common.ssh;

import org.envtools.monitor.common.encrypting.EncryptionServiceImpl;
import org.envtools.monitor.common.jaxb.JaxbHelper;

import javax.xml.bind.JAXBException;
import java.io.File;

/**
 * Created by Michal Skuza on 2016-06-17.
 */
public class SshCredentialsServiceImpl implements SshCredentialsService {


    private final String masterKey = "jasypt.master.key";
    private final String algorithm = "jasypt.algorithm";
    private final String directoryPath;


    private final EncryptionServiceImpl encryptionService =
            new EncryptionServiceImpl(System.getProperty(masterKey), System.getProperty(algorithm));

    public SshCredentialsServiceImpl(String credentialsDirectory) {
        this.directoryPath = credentialsDirectory;
    }

    @Override
    public Credentials getCredentials(String host) {
        try {

            Credentials credentials
                    = JaxbHelper.unmarshallFromFile(getCredentialFile(host), Credentials.class, null);
            credentials.setPassword(encryptionService.decrypt(credentials.getEncryptedPassword()));

            return credentials;

        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }
    }

    private File getCredentialFile(String host) {
        return new File(String.format("%s%s.xml", directoryPath, host));
    }
}
