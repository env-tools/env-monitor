package org.envtools.monitor.common.util;

import com.google.common.base.Preconditions;
import org.envtools.monitor.common.encrypting.EncryptionService;

import java.util.Map;

/**
 * Created: 13.03.16 2:56
 *
 * @author Yury Yakovlev
 */
public final class PropertyUtil {

    public static final String ENCRYPTION_PREFIX = "_encrypted_:";
    private PropertyUtil() {
    }

    public static String getRequiredValue(Map<String, String> map, String key,
                                            EncryptionService encryptionService) {
        if (map.containsKey(key)) {
            String value = map.get(key);

            if (value.startsWith(ENCRYPTION_PREFIX)) {
                Preconditions.checkNotNull(encryptionService, "No encryption service injected");
                String encryptedValue = value.substring(ENCRYPTION_PREFIX.length());
                value = encryptionService.decrypt(encryptedValue);
            }

            return value;
        } else {
            throw new IllegalArgumentException(
                    String.format(
                            "Value for required key %s not found, present keys: %s",
                            key, map.keySet()));
        }
    }

}
