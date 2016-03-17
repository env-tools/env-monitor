package org.envtools.monitor.ui.controller;

import org.apache.log4j.Logger;
import org.envtools.monitor.model.messaging.ResponseMessage;
import org.envtools.monitor.model.messaging.ResponsePayload;
import org.envtools.monitor.module.core.ApplicationsModuleStorageService;
import org.envtools.monitor.module.core.selection.DestinationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.messaging.simp.config.AbstractMessageBrokerConfiguration;
import org.springframework.stereotype.Controller;

import javax.annotation.PostConstruct;

/**
 * Created: 17.03.16 1:16
 *
 * @author Yury Yakovlev
 */
@Controller
public class WebSocketCallController {

    private static final Logger LOGGER = Logger.getLogger(WebSocketCallController.class);

    @Autowired
    AbstractMessageBrokerConfiguration configuration;

    @Autowired
    ApplicationsModuleStorageService applicationsModuleStorageService;

    //TODO clarify whether it is needed
    @PostConstruct
    public void init() {
        configuration.simpleBrokerMessageHandler().start();
    }

    /**
     * This method supports one-time data request by selector
     * Clients should subscribe with "/app" prefix
     *
     * @param headerAccessor Means to extract request details
     * @return Extracted data matching the selector
     */
    @SubscribeMapping("/modules/M_APPLICATIONS" + DestinationUtil.SELECTOR_SEPARATOR + "**")
    public ResponseMessage handleApplicationsModuleCall(SimpMessageHeaderAccessor headerAccessor
    ) {

        String sessId = headerAccessor.getSessionId();
        String destination = headerAccessor.getDestination();
        String selector = DestinationUtil.extractSelector(destination);

        LOGGER.info(String.format("WebSocketCallController.handleApplicationsModuleCall - received call from client %s for destination %s, selector %s",
                sessId, destination, selector));
        ResponseMessage responseMessage = new ResponseMessage.Builder()
                .sessionId(sessId)
                .payload(new ResponsePayload(null, applicationsModuleStorageService.extractSerializedPartBySelector(selector)))
                .build();
        return responseMessage;
    }

    @SubscribeMapping("/modules/M_QUERY_LIBRARY/**")
    public ResponseMessage handleQueryLibraryModuleCall(SimpMessageHeaderAccessor headerAccessor
    ) {
       //TODO implement
        return null;
    }


}
