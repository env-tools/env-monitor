package org.envtools.monitor.module.core.subscription;


import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * Created: 12/11/15 12:33 PM
 *
 * @author Yury Yakovlev
 */
@Component
public class SubscriptionManagerImpl implements SubscriptionManager {

    private static final Logger LOGGER = Logger.getLogger(SubscriptionManagerImpl.class);

    private Multimap<String, String> subscribersByDestination = ArrayListMultimap.create();

    @Override
    public void registerSubscription(String sessionId, String destination) {
        subscribersByDestination.put(destination, sessionId);

        LOGGER.info(String.format("SubscriptionManagerImpl.registerSubscription - registered subscription to %s by %s, remaining: %s",
                destination, sessionId, subscribersByDestination));

    }

    @Override
    public void unregisterSubscription(String sessionId, String destination) {
        subscribersByDestination.remove(destination, sessionId);

        LOGGER.info(String.format("SubscriptionManagerImpl.unregisterSubscription - unregistered subscription to %s by %s, remaining: %s",
                destination, sessionId, subscribersByDestination));
    }

    @Override
    public boolean hasSubscribers(String destination) {
        return subscribersByDestination.containsKey(destination);
    }

    @Override
    public Set<String> getSubscribedDestinations() {
        return subscribersByDestination.keySet();
    }
}
