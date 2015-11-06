package org.envtools.monitor.ui.controller;

import org.envtools.monitor.model.messaging.DataRequestMessage;
import org.envtools.monitor.module.ModuleConstants;
import org.envtools.monitor.module.core.CoreModuleRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.messaging.simp.broker.SimpleBrokerMessageHandler;
import org.springframework.messaging.simp.broker.SubscriptionRegistry;
import org.springframework.messaging.simp.config.AbstractMessageBrokerConfiguration;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.stereotype.Controller;
import org.apache.log4j.Logger;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;


/**
 * Created: 9/19/15 11:50 PM
 *
 * @author Yury Yakovlev
 */
@Controller
public class WebSocketAppController implements ApplicationListener<SessionSubscribeEvent>{

    private static final Logger LOGGER = Logger.getLogger(WebSocketAppController.class);

    @Autowired
    ApplicationContext ctx;

    @Resource(name = "clientInboundChannel")
    SubscribableChannel clientInboundChannel;

    @Resource(name = "clientOutboundChannel")
    SubscribableChannel clientOutboundChannel;

    @Autowired
    AbstractMessageBrokerConfiguration configuration;

    @Autowired
    CoreModuleRunner coreModuleRunner;

    @PostConstruct
    public void init() {
        LOGGER.info("WebSocketAppController.init - WebSocketAppController has been initialized. ");

        configuration.simpleBrokerMessageHandler().start();

        clientInboundChannel.subscribe(message -> LOGGER.info("Message from browser: " + message));
        clientOutboundChannel.subscribe(message -> LOGGER.info("Message to browser: " + message));
    }

    @Resource(name = "applications.channel")
    MessageChannel applicationsModuleDataRequestChannel;

//    @SubscribeMapping("/topic/modulepush")
//    public void handleSubscription() {
//
//    }

    //WebSocket message mapping
    @MessageMapping("/modulerequest")
    public void handleDataRequest(@Payload DataRequestMessage dataRequestMessage,
                                  SimpMessageHeaderAccessor headerAccessor)
    {
        String sessionId = headerAccessor.getSessionId();
        dataRequestMessage.setSessionId(sessionId);
        LOGGER.info("WebSocketAppController.handleDataRequest - request : " + dataRequestMessage);

        String targetModuleId = dataRequestMessage.getTargetModuleId();
        if (targetModuleId == null) {
            LOGGER.error("WebSocketAppController.handleDataRequest - target module id is not specified, ignoring");
            return;
        }

        //Send data request to the appropriate module
        switch(dataRequestMessage.getTargetModuleId()) {
            case ModuleConstants.APPLICATIONS_MODULE_ID:
                applicationsModuleDataRequestChannel.send(new GenericMessage<DataRequestMessage>(dataRequestMessage));
                break;
            default:
                LOGGER.error("WebSocketAppController.handleDataRequest - target module id is not supported");
        }

    }

    @Override
    public void onApplicationEvent(SessionSubscribeEvent sessionSubscribeEvent) {
        Message<byte[]> message = sessionSubscribeEvent.getMessage();
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        StompCommand command = accessor.getCommand();
        if (command.equals(StompCommand.SUBSCRIBE)) {
            String sessionId = accessor.getSessionId();
            String stompSubscriptionId = accessor.getSubscriptionId();
            String destination = accessor.getDestination();
            LOGGER.info(String.format(
                    "WebSocketAppController.onSubscriptionEvent - session %s with stomp subscription id %s subscribed to destination %s",
                    sessionId,
                    stompSubscriptionId,
                    destination));
        }
    }

}
