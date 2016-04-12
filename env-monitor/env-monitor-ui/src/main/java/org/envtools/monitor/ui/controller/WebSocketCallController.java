package org.envtools.monitor.ui.controller;

import org.apache.log4j.Logger;
import org.envtools.monitor.model.messaging.ResponseMessage;
import org.envtools.monitor.model.messaging.ResponsePayload;
import org.envtools.monitor.module.core.cache.ApplicationsModuleStorageService;
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

    // Services for APPLICATIONS module

    @Autowired
    ApplicationsModuleStorageService applicationsModuleStorageService;

    // Services for QUERY_LIBRARY module
    // ...

    //TODO clarify whether it is needed
    @PostConstruct
    public void init() {
        configuration.simpleBrokerMessageHandler().start();
    }

    /**
     * This method supports one-time data request by selector
     * Clients should subscribe with "/call" prefix
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
        ResponsePayload responsePayload = ResponsePayload.builder().jsonContent(applicationsModuleStorageService.extractPartBySelector(selector))
                .build();
        ResponseMessage responseMessage = new ResponseMessage.Builder()
                .sessionId(sessId)
                .payload(responsePayload)
                .build();
        return responseMessage;
    }

    /**
     * This method supports one-time request to Query Library module
     * Clients should subscribe with "/call" prefix
     * <p>
     * Not used yet
     */
    @SubscribeMapping("/modules/M_QUERY_LIBRARY/**")
    public ResponseMessage handleQueryLibraryModuleCall(SimpMessageHeaderAccessor headerAccessor
    ) {
        //TODO implement
        return null;
    }


}
