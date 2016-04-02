package org.envtools.monitor.module.core.selection;

import org.envtools.monitor.module.core.cache.QueryLibraryDataPushService;
import org.omg.CORBA.portable.Streamable;

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

    public static QueryLibraryDestinationData parseQlDestination(String destination) {
        QueryLibraryDestinationData destinationData = new QueryLibraryDestinationData();

        String owner = destination.substring(destination.lastIndexOf('/') + 1);
        String withoutOwner = destination.substring(0, destination.lastIndexOf('/'));
        String action = withoutOwner.substring(withoutOwner.lastIndexOf('/') + 1);

        destinationData.setAction(action);
        destinationData.setOwner(owner);

        return destinationData;
    }
}
