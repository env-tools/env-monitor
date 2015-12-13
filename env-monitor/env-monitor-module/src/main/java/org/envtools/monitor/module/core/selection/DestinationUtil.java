package org.envtools.monitor.module.core.selection;

/**
 * Created: 12/10/15 9:08 PM
 *
 * @author Yury Yakovlev
 */
public final class DestinationUtil {

    public static final String SELECTOR_SEPARATOR = "/data/";

    private DestinationUtil() {
    }

    public static String extractSelector(String destination) {

        if (!destination.contains(SELECTOR_SEPARATOR)) {
            return "";
        } else {
            return destination.substring(destination.indexOf(SELECTOR_SEPARATOR) + SELECTOR_SEPARATOR.length());
        }

    }

    public static boolean isDestinationForModule(String destination, String moduleId) {
        return destination.contains(String.format("/modules/%s", moduleId));
    }
}
