package org.envtools.monitor.ui.controller.util;

import com.google.common.collect.Maps;
import org.apache.log4j.Logger;
import org.envtools.monitor.module.ModuleConstants;
import org.envtools.monitor.common.serialization.aggregator.Aggregator;
import org.envtools.monitor.module.core.cache.ApplicationsDataPushService;
import org.envtools.monitor.module.core.cache.ApplicationsModuleStorageService;
import org.envtools.monitor.module.core.cache.QueryLibraryDataPushService;
import org.envtools.monitor.module.core.cache.QueryLibraryModuleStorageService;
import org.envtools.monitor.module.core.selection.DestinationUtil;
import org.envtools.monitor.module.core.selection.QueryLibraryDestinationData;
import org.envtools.monitor.module.core.subscription.SubscriptionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.AbstractSubProtocolEvent;
import org.springframework.web.socket.messaging.SessionUnsubscribeEvent;

import javax.annotation.PreDestroy;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created: 12/11/15 1:12 PM
 *
 * @author Yury Yakovlev
 */
@Component
public class StompSubscriptionCommandHandler {

    private static final Logger LOGGER = Logger.getLogger(StompSubscriptionCommandHandler.class);

    @Autowired
    private ApplicationsDataPushService applicationsDataPushService;

    @Autowired
    private QueryLibraryDataPushService queryLibraryDataPushService;

    @Autowired
    ApplicationsModuleStorageService applicationsModuleStorageService;

    @Autowired
    QueryLibraryModuleStorageService queryLibraryModuleStorageService;

    @Autowired
    SubscriptionManager subscriptionManager;

    @Autowired
    Aggregator jsonAggregator;

    private ScheduledExecutorService executor = Executors.newScheduledThreadPool(5);

    private Map<String, String> destinationsBySubId = Maps.newConcurrentMap();

    public static boolean isSubscriptionCommand(StompCommand stompCommand) {
        return isSubscribe(stompCommand) || isUnSubscribe(stompCommand);
    }

    public static boolean isSubscribe(StompCommand stompCommand) {
        return stompCommand.equals(StompCommand.SUBSCRIBE);
    }

    public static boolean isUnSubscribe(StompCommand stompCommand) {
        return stompCommand.equals(StompCommand.UNSUBSCRIBE);
    }

    /**
     * This method handles continuous subscriptions/unsubscriptions from clients, for all modules
     * Subscriptions must be registered so that data updated could be sent to them
     *
     * @param sessionEvent event containing STOMP subscription information
     */
    public void handleSubscription(AbstractSubProtocolEvent sessionEvent) {
        Message<byte[]> message = sessionEvent.getMessage();
        StompHeaderAccessor stompAccessor = StompHeaderAccessor.wrap(message);
        StompCommand stompCommand = stompAccessor.getCommand();

        if (StompSubscriptionCommandHandler.isSubscriptionCommand(stompCommand)) {
            processSubscriptionCommand(stompCommand, stompAccessor);
        }

    }

    public void processSubscriptionCommand(StompCommand stompCommand, StompHeaderAccessor stompHeaderAccessor) {

        //This is unique identifier of browser websocket session
        String sessionId = stompHeaderAccessor.getSessionId();

        //This is unique identifier for single websocket subscription
        String stompSubscriptionId = stompHeaderAccessor.getSubscriptionId();

        //Destination
        String destination = resolveDestination(stompHeaderAccessor, stompSubscriptionId);

        LOGGER.info(String.format(
                "StompSubscriptionCommandHandler.processSubscriptionCommand - %s - session %s with stomp subscription id %s for destination %s",
                stompCommand,
                sessionId,
                stompSubscriptionId,
                destination));

        if (destination == null) {
            LOGGER.error("StompSubscriptionCommandHandler.processSubscriptionCommand - could not resolve destination, skipping processing for " + stompCommand);
            return;
        }

        if (DestinationUtil.isDestinationForModule(destination, ModuleConstants.APPLICATIONS_MODULE_ID)) {

            processApplicationsModuleSubscription(destination, stompCommand, sessionId);

        } else if (DestinationUtil.isDestinationForModule(destination, ModuleConstants.QUERY_LIBRARY_MODULE_ID)) {

            processQueryLibraryModuleSubscription(destination, stompCommand, sessionId);

        } else {

            LOGGER.warn("StompSubscriptionCommandHandler.processSubscriptionCommand - destination module not specified or not supported in destination " + destination);

        }
    }

    private String resolveDestination(StompHeaderAccessor stompHeaderAccessor, String stompSubscriptionId) {

        String destination = stompHeaderAccessor.getDestination();
        //Destination is present for subscription commands
        if (destination == null) {
            destination = destinationsBySubId.get(stompSubscriptionId);
        } else {
            destinationsBySubId.put(stompSubscriptionId, destination);
        }

        return destination;
    }

    private void processApplicationsModuleSubscription(String destination, StompCommand stompCommand, String sessionId) {

        String selector = DestinationUtil.extractSelector(destination);
        LOGGER.info(String.format(
                "StompSubscriptionCommandHandler.processApplicationsModuleSubscription - selector =  %s", selector));

        if (isSubscribe(stompCommand)) {
            subscriptionManager.registerSubscription(sessionId, destination);

            //TODO: rework to run instantly after subscription has been internally processed (in the same thread)!

            //Send initial data immediately after subscription has been processed (how to do it more gracefully? )
            executor.schedule(() -> {
                sendDataToSubscriberImmediately(destination);
            }, 100, TimeUnit.MILLISECONDS);


        } else if (isUnSubscribe(stompCommand)) {
            subscriptionManager.unregisterSubscription(sessionId, destination);
        }
    }

    private void processQueryLibraryModuleSubscription(String destination, StompCommand stompCommand, String sessionId) {
        QueryLibraryDestinationData destinationData = DestinationUtil.parseQlDestination(destination);
        String action = destinationData.getAction();

        if (isSubscribe(stompCommand)) {
            subscriptionManager.registerSubscription(sessionId, destination);
        } else if (isUnSubscribe(stompCommand)) {
            subscriptionManager.unregisterSubscription(sessionId, destination);
        }

        switch (action) {
            case "tree":
                executor.schedule(() -> {
                    queryLibraryDataPushService.sendTreeDataToSubscribers();
                }, 100, TimeUnit.MILLISECONDS);
                break;
            default:
                LOGGER.warn("StompSubscriptionCommandHandler.processQueryLibraryModuleSubscription - unsupported  action, skipping : " + action);
                break;
        }
    }

    private void sendDataToSubscriberImmediately(String subscribedDestination) {

        String contentPart = applicationsModuleStorageService.extractPartBySelector(
                DestinationUtil.extractSelector(subscribedDestination));

        applicationsDataPushService.pushToSubscribedClients(subscribedDestination, contentPart);

    }


    @PreDestroy
    public void destroy() {
        executor.shutdown();
    }
}
