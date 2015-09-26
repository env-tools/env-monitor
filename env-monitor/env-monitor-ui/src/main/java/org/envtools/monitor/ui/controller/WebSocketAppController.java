package org.envtools.monitor.ui.controller;

import org.envtools.monitor.model.ModuleRequest;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;
import org.apache.log4j.Logger;


/**
 * Created: 9/19/15 11:50 PM
 *
 * @author Yury Yakovlev
 */
@Controller
public class WebSocketAppController {

    private static Logger LOGGER = Logger.getLogger(WebSocketAppController.class);

    @MessageMapping("/modulerequest")
    @SendToUser(value = "/topic/moduleresponse", broadcast = false)
    public ModuleRequest sendModuleRequest(@Payload ModuleRequest request) {
        LOGGER.info("WebSocketAppController.sendModuleRequest - request : " + request);
        return request;
    }
}
