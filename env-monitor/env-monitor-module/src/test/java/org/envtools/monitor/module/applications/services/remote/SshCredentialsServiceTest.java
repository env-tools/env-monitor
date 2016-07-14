package org.envtools.monitor.module.applications.services.remote;

import org.envtools.monitor.common.encrypting.EncryptionServiceImpl;
import org.envtools.monitor.module.applications.services.Credentials;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by esemenov on 14.07.16.
 */
public class SshCredentialsServiceTest {


    //TODO: Finish the test;

    private final String masterKey = "jasypt.master.key";
    private final String algorithm = "jasypt.algorithm";
    private final String filePath = "";

    private EncryptionServiceImpl encryptionService;


    @Before
    public void setUp() {

        Credentials credentials = new Credentials();

        credentials.setHost("some host");
        credentials.setUser("some user");


    }

    @Test
    public void decryptPasswordTest() {


    }


}
