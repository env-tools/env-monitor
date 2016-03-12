package org.envtools.monitor.common.util;

import java.util.Map;

/**
 * Created: 13.03.16 2:56
 *
 * @author Yury Yakovlev
 */
public final class PropertyUtil {

    private PropertyUtil() {
    }

    public static <K,V> V getRequiredValue(Map<K,V> map, K key) {
        if (map.containsKey(key)) {
            return map.get(key);
        } else {
            throw new IllegalArgumentException(
                    String.format(
                            "Value for required key %s not found, present keys: %s",
                            key, map.keySet()));
        }
    }
}
