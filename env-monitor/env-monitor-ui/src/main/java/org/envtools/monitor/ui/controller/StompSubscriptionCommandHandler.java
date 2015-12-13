package org.envtools.monitor.ui.controller;

import org.apache.log4j.Logger;
import org.envtools.monitor.module.ModuleConstants;
import org.envtools.monitor.module.core.selection.DestinationUtil;
import org.envtools.monitor.module.core.subscription.SubscriptionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;

/**
 * Created: 12/11/15 1:12 PM
 *
 * @author Yury Yakovlev
 */
@Component
public class StompSubscriptionCommandHandler {

    private static final Logger LOGGER = Logger.getLogger(StompSubscriptionCommandHandler.class);

    @Autowired
    SubscriptionManager subscriptionManager;

    public static boolean isSubscriptionCommand(StompCommand stompCommand) {
        return isSubscribe(stompCommand) || isUnSubscribe(stompCommand);
    }

    public static boolean isSubscribe(StompCommand stompCommand) {
        return stompCommand.equals(StompCommand.SUBSCRIBE);
    }

    public static boolean isUnSubscribe(StompCommand stompCommand) {
        return stompCommand.equals(StompCommand.UNSUBSCRIBE);
    }

    public void processSubscriptionCommand(StompCommand stompCommand, StompHeaderAccessor stompHeaderAccessor) {
        String sessionId = stompHeaderAccessor.getSessionId();
        String stompSubscriptionId = stompHeaderAccessor.getSubscriptionId();
        String destination = stompHeaderAccessor.getDestination();
        String selector = DestinationUtil.extractSelector(destination);

        LOGGER.info(String.format(
                "StompSubscriptionCommandHandler.processSubscriptionCommand - %s - session %s with stomp subscription id %s for destination %s (selector %s)",
                stompCommand,
                sessionId,
                stompSubscriptionId,
                destination,
                selector));

        if (DestinationUtil.isDestinationForModule(destination, ModuleConstants.APPLICATIONS_MODULE_ID)) {

            if (isSubscribe(stompCommand)) {
                subscriptionManager.registerSubscription(sessionId, destination);
            } else if (isUnSubscribe(stompCommand)) {
                subscriptionManager.unregisterSubscription(sessionId, destination);
            }

        } else {
            LOGGER.warn("WebSocketAppController.handleSubscription - destination module not specified or not supported in destination " + destination);
        }
    }
}
