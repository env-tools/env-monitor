package org.envtools.monitor.common.encrypting;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;

/**
 * Created by esemenov on 08.07.16.
 */
public class EncryptionServiceImpl implements EncryptionService {

    StandardPBEStringEncryptor encryptor;

    public EncryptionServiceImpl(String masterKey, String algorithm) {

        StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
        encryptor.setPassword(masterKey);
        encryptor.setAlgorithm(algorithm);

    }

    @Override
    public String encrypt(String decryptedString) {

        return encryptor.encrypt(decryptedString);
    }

    @Override
    public String decrypt(String encryptedString) {
        return decrypt(encryptedString);
    }
}
