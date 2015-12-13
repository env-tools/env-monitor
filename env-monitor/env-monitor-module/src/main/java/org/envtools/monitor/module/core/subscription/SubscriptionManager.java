package org.envtools.monitor.module.core.subscription;

import java.util.Set;

/**
 * Created: 12/10/15 9:13 PM
 *
 * @author Yury Yakovlev
 */
public interface SubscriptionManager {

    void registerSubscription(String sessionId, String destination);

    void unregisterSubscription(String sessionId, String destination);

    boolean hasSubscribers(String destination);

    Set<String> getSubscribedDestinations();

}
