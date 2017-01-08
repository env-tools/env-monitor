package org.envtools.monitor.common.encrypting;

/**
 * Created by Dray on 08.07.16.
 */
public interface EncryptionService {

    String encrypt(String decryptedString);

    String decrypt(String encryptedString);

}
