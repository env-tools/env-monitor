package org.envtools.monitor.common.encrypting;


import org.jasypt.exceptions.EncryptionOperationNotPossibleException;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

import java.io.IOException;

/**
 * Created by esemenov on 12.07.16.
 */
public class PasswordEncryptorTest {


    private static final String PROP_FILE = "src/test/resource/encryptor.properties";
    private static final String MASTER_KEY = "jasypt.master.key";
    private static final String ALGORITHM = "jasypt.algorithm";
    private static final String PLAIN_TEXT = "decrypted.text";

    private static String algorithm;
    private static EncryptionServiceImpl encryptionService;
    private static String decryptedText;


    @BeforeClass
    public static void init() throws IOException {

        String masterKey;
        Properties prop = new Properties();

        try (InputStream input = new FileInputStream(PROP_FILE)) {
            prop.load(input);
        }

        masterKey = prop.getProperty(MASTER_KEY);
        algorithm = prop.getProperty(ALGORITHM);
        decryptedText = prop.getProperty(PLAIN_TEXT);

        if ((masterKey == null) || (algorithm == null) || (decryptedText == null))
            throw new IOException();

        encryptionService = new EncryptionServiceImpl(masterKey, algorithm);

    }


    @Test
    public void encryptingTest() {

        String passwordHash = encryptionService.encrypt(decryptedText);
        Assert.assertEquals(decryptedText, encryptionService.decrypt(passwordHash));

    }


    @Test(expected = EncryptionOperationNotPossibleException.class)
    public void wrongMasterKeyTest() {

        EncryptionService wrongKeyEncryptor = new EncryptionServiceImpl("wrongKey", algorithm);
        String passwordHash = encryptionService.encrypt(decryptedText);
        wrongKeyEncryptor.decrypt(passwordHash);
    }

}
