package org.envtools.monitor.ui.controller;

import org.envtools.monitor.model.messaging.DataRequestMessage;
import org.envtools.monitor.module.ModuleConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.stereotype.Controller;
import org.apache.log4j.Logger;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;


/**
 * Created: 9/19/15 11:50 PM
 *
 * @author Yury Yakovlev
 */
@Controller
public class WebSocketAppController {

    private static final Logger LOGGER = Logger.getLogger(WebSocketAppController.class);

    @Autowired
    ApplicationContext ctx;

    @Resource(name = "clientInboundChannel")
    MessageChannel clientInboundChannel;

    @Resource(name = "clientOutboundChannel")
    MessageChannel clientOutboundChannel;

    @PostConstruct
    public void init() {
        LOGGER.info("WebSocketAppController.init - WebSocketAppController has been initialized. ");
    }

    @Resource(name = "applications.channel")
    MessageChannel applicationsModuleDataRequestChannel;

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
}
