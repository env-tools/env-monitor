package org.envtools.monitor.module.core.cache;

import org.envtools.monitor.common.serialization.aggregator.Aggregator;
import org.envtools.monitor.module.core.selection.DestinationUtil;
import org.envtools.monitor.module.core.selection.QueryLibraryDestinationData;
import org.envtools.monitor.module.core.subscription.SubscriptionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created: 3/19/16 3:55 AM
 *
 * @author Sergey Moldachev
 */
@Service
public class QueryLibraryDataPushService extends AbstractPushService {

    @Autowired
    QueryLibraryModuleStorageService queryLibraryModuleStorageService;

    @Autowired
    SubscriptionManager subscriptionManager;

    @Autowired
    Aggregator jsonAggregator;

    public void sendTreeDataToSubscribers() {
        for (String destination : subscriptionManager.getSubscribedDestinations()) {
            QueryLibraryDestinationData destinationData = DestinationUtil.parseQlDestination(destination);
            String action = destinationData.getAction();
            if (action.equals("tree")) {
                String publicTree = queryLibraryModuleStorageService.getPublicTree();
                String privateTree = queryLibraryModuleStorageService.getTreeByOwner(destinationData.getOwner());

                String contentPart = jsonAggregator.aggregate(publicTree, privateTree);

                pushToSubscribedClients(destination, contentPart);
            }
        }
    }
}
