package org.envtools.monitor.ui.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.Message;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;

/**
 * Created: 17.03.16 3:56
 *
 * @author Yury Yakovlev
 */
@Component
public class WebSocketSubscriptionController implements ApplicationListener<SessionSubscribeEvent> {

    private static final Logger LOGGER = Logger.getLogger(WebSocketSubscriptionController.class);

    @Autowired
    StompSubscriptionCommandHandler stompSubscriptionCommandHandler;

    @Override
    public void onApplicationEvent(SessionSubscribeEvent sessionSubscribeEvent) {

        handleSubscription(sessionSubscribeEvent);

    }

    /**
     * This method handles continuous subscriptions from clients
     * Subscriptions must be registered so that data updated could be sent to them
     *
     * @param sessionSubscribeEvent event containing STOMP subscription information
     */
    private void handleSubscription(SessionSubscribeEvent sessionSubscribeEvent) {
        Message<byte[]> message = sessionSubscribeEvent.getMessage();
        StompHeaderAccessor stompAccessor = StompHeaderAccessor.wrap(message);
        StompCommand stompCommand = stompAccessor.getCommand();

        if (StompSubscriptionCommandHandler.isSubscriptionCommand(stompCommand)) {
            stompSubscriptionCommandHandler.processSubscriptionCommand(stompCommand, stompAccessor);
        }
    }

}
