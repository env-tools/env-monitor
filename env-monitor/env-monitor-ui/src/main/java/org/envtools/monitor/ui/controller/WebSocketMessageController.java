package org.envtools.monitor.ui.controller;

import org.apache.log4j.Logger;
import org.envtools.monitor.model.messaging.RequestMessage;
import org.envtools.monitor.module.ModuleConstants;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;

/**
 * Created: 17.03.16 1:21
 *
 * @author Yury Yakovlev
 */
@Controller
public class WebSocketMessageController {

    private static final Logger LOGGER = Logger.getLogger(WebSocketMessageController.class);

    @Resource(name = "applications.channel")
    MessageChannel applicationsModuleChannel;

    @Resource(name = "querylibrary.channel")
    MessageChannel queryLibraryModuleChannel;

    //WebSocket message mapping
    @MessageMapping("/modulerequest")
    public void handleMessage(@Payload RequestMessage requestMessage,
                              SimpMessageHeaderAccessor headerAccessor) {
        String sessionId = headerAccessor.getSessionId();
        requestMessage.setSessionId(sessionId);
        LOGGER.info("WebSocketMessageController.handleMessage - request : " + requestMessage);

        String targetModuleId = requestMessage.getTargetModuleId();
        if (targetModuleId == null) {
            LOGGER.error("WebSocketMessageController.handleMessage - target module id is not specified, ignoring");
            return;
        }

        //Send data request to the appropriate module
        switch (targetModuleId) {
            case ModuleConstants.APPLICATIONS_MODULE_ID:
                handleApplicationsModuleMessage(requestMessage);
                break;
            case ModuleConstants.QUERY_LIBRARY_MODULE_ID:
                handleQueryLibraryModuleMessage(requestMessage);
                break;
            default:
                LOGGER.error("WebSocketMessageController.handleMessage - target module id is not supported");
        }

    }

    private void handleApplicationsModuleMessage(RequestMessage requestMessage) {

        applicationsModuleChannel.send(new GenericMessage<RequestMessage>(requestMessage));

    }

    private void handleQueryLibraryModuleMessage(RequestMessage requestMessage) {

        queryLibraryModuleChannel.send(new GenericMessage<RequestMessage>(requestMessage));

    }

}
