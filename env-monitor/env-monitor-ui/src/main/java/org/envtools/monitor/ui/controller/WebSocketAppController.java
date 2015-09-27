package org.envtools.monitor.ui.controller;

import org.envtools.monitor.model.ModuleRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;
import org.apache.log4j.Logger;

import java.security.Principal;
import java.util.Collections;


/**
 * Created: 9/19/15 11:50 PM
 *
 * @author Yury Yakovlev
 */
@Controller
public class WebSocketAppController {

    private static Logger LOGGER = Logger.getLogger(WebSocketAppController.class);

    @Autowired
    private SimpMessagingTemplate template;

    @MessageMapping("/modulerequest")
    //@SendToUser(value = "/topic/moduleresponse", broadcast = false)
    public void sendModuleRequest(@Payload ModuleRequest request,
                                           SimpMessageHeaderAccessor headerAccessor
                                           /* ,Principal principal*/) {
        String sessionId = headerAccessor.getSessionId();
        request.setSessionId(sessionId);
        LOGGER.info("WebSocketAppController.sendModuleRequest - request : " + request);

        String user = sessionId;
        String destination = "/topic/moduleresponse";
        template.convertAndSendToUser(user, destination, request,
                createHeaders(sessionId) );
                //Collections.<String, Object>singletonMap(
                //        SimpMessageHeaderAccessor.SESSION_ID_HEADER, sessionId));

        //return request;
    }

    private MessageHeaders createHeaders(String sessionId) {
        SimpMessageHeaderAccessor headerAccessor = SimpMessageHeaderAccessor.create(SimpMessageType.MESSAGE);
        headerAccessor.setSessionId(sessionId);
        headerAccessor.setLeaveMutable(true);
        return headerAccessor.getMessageHeaders();
    }

}
