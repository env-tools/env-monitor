package org.envtools.monitor.ui.controller;

import org.apache.log4j.Logger;
import org.envtools.monitor.ui.controller.util.StompSubscriptionCommandHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.Message;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;
import org.springframework.web.socket.messaging.SessionUnsubscribeEvent;

/**
 * Created: 06.01.17 0:20
 *
 * @author Yury Yakovlev
 */
@Component
public class WebSocketUnsubscriptionController implements ApplicationListener<SessionUnsubscribeEvent> {

    private static final Logger LOGGER = Logger.getLogger(WebSocketUnsubscriptionController.class);

    /**
     * This handler is a service responsible for handling all unsubscription commands to all modules
     */
    @Autowired
    StompSubscriptionCommandHandler stompSubscriptionCommandHandler;

    @Override
    public void onApplicationEvent(SessionUnsubscribeEvent sessionUnsubscribeEvent) {

        //Any unsubscription request from web socket client (with destination started with "/subscribe") will go here
        stompSubscriptionCommandHandler.handleSubscription(sessionUnsubscribeEvent);

    }

}
