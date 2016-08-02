package org.envtools.monitor.common.ssh;

import org.envtools.monitor.common.encrypting.EncryptionServiceImpl;
import org.envtools.monitor.common.jaxb.JaxbHelper;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by Michal Skuza on 2016-06-17.
 */
public class SshCredentialsServiceImpl implements SshCredentialsService {
    private final String directoryPath;
    private Properties prop;

//    private static final String PROP_FILE = "src/main/resources/application.properties";
//    private static final String MASTER_KEY = "jasypt.master.key";
//    private static final String ALGORITHM = "jasypt.algorithm";

//    {
//        prop = new Properties();
//
//        try (InputStream input = new FileInputStream(PROP_FILE)) {
//
//            prop.load(input);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

//    private final EncryptionServiceImpl encryptionService = new EncryptionServiceImpl(prop.getProperty(MASTER_KEY), prop.getProperty(ALGORITHM));

    public SshCredentialsServiceImpl(String credentialsDirectory) {
        this.directoryPath = credentialsDirectory;
    }

    @Override
    public Credentials getCredentials(String host) {
        try {
            Credentials credentials = JaxbHelper.unmarshallFromFile(getCredentialFile(host), Credentials.class, null);
//            credentials.setPassword(encryptionService.decrypt(credentials.getEncryptedPassword()));
            credentials.setPassword(credentials.getEncryptedPassword());
            return credentials;

        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }
    }

    private File getCredentialFile(String host) {
        return new File(String.format("%s%s%s.xml", directoryPath, File.separator, host));
    }
}
