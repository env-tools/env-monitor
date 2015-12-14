package org.envtools.monitor.ui.controller;

import org.envtools.monitor.model.messaging.RequestMessage;
import org.envtools.monitor.model.messaging.ResponseMessage;
import org.envtools.monitor.model.messaging.ResponsePayload;
import org.envtools.monitor.module.ModuleConstants;
import org.envtools.monitor.module.core.ApplicationsModuleDataService;
import org.envtools.monitor.module.core.CoreModuleRunner;
import org.envtools.monitor.module.core.selection.DestinationUtil;
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
public class WebSocketAppController implements ApplicationListener<SessionSubscribeEvent> {

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

    @Autowired
    ApplicationsModuleDataService applicationsModuleDataService;

    @Autowired
    StompSubscriptionCommandHandler stompSubscriptionCommandHandler;

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
    public void handleRequest(@Payload RequestMessage requestMessage,
                              SimpMessageHeaderAccessor headerAccessor) {
        String sessionId = headerAccessor.getSessionId();
        requestMessage.setSessionId(sessionId);
        LOGGER.info("WebSocketAppController.handleRequest - request : " + requestMessage);

        String targetModuleId = requestMessage.getTargetModuleId();
        if (targetModuleId == null) {
            LOGGER.error("WebSocketAppController.handleRequest - target module id is not specified, ignoring");
            return;
        }

        //Send data request to the appropriate module
        switch (requestMessage.getTargetModuleId()) {
            case ModuleConstants.APPLICATIONS_MODULE_ID:
                applicationsModuleDataRequestChannel.send(new GenericMessage<RequestMessage>(requestMessage));
                break;
            default:
                LOGGER.error("WebSocketAppController.handleRequest - target module id is not supported");
        }

    }

    /**
     * This method supports one-time data request by selector
     * Clients should subscribe with "/app" prefix
     *
     * @param headerAccessor Means to extract request details
     * @return Extracted data matching the selector
     */
    @SubscribeMapping("/modules/M_APPLICATIONS" + DestinationUtil.SELECTOR_SEPARATOR + "**")
    public ResponseMessage handleCall(SimpMessageHeaderAccessor headerAccessor
    ) {

        String sessId = headerAccessor.getSessionId();
        String destination = headerAccessor.getDestination();
        String selector = DestinationUtil.extractSelector(destination);

        LOGGER.info(String.format("WebSocketAppController.handleCall - received call from client %s for destination %s, selector %s",
                sessId, destination, selector));
        ResponseMessage responseMessage = new ResponseMessage.Builder()
                .sessionId(sessId)
                .payload(new ResponsePayload(null, applicationsModuleDataService.extractSerializedPartBySelector(selector)))
                .build();
        return responseMessage;
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

    @Override
    public void onApplicationEvent(SessionSubscribeEvent sessionSubscribeEvent) {
        handleSubscription(sessionSubscribeEvent);
    }

}
