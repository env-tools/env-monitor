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

/**
 * Created: 17.03.16 3:56
 *
 * @author Yury Yakovlev
 */
@Component
public class WebSocketSubscriptionController implements ApplicationListener<SessionSubscribeEvent> {

    private static final Logger LOGGER = Logger.getLogger(WebSocketSubscriptionController.class);

    /**
     * This handler is a service responsible for handling all subscription commands to all modules
     */
    @Autowired
    StompSubscriptionCommandHandler stompSubscriptionCommandHandler;

    @Override
    public void onApplicationEvent(SessionSubscribeEvent sessionSubscribeEvent) {

        //Any subscription request from web socket client (with destination started with "/subscribe") will go here
        stompSubscriptionCommandHandler.handleSubscription(sessionSubscribeEvent);

    }

}
