package org.envtools.monitor.module.core.cache;

import org.apache.log4j.Logger;
import org.envtools.monitor.model.messaging.ResponseMessage;
import org.envtools.monitor.model.messaging.ResponsePayload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;

/**
 * Created: 3/19/16 3:57 AM
 *
 * @author Yury Yakovlev
 */
public abstract class AbstractPushService implements DataPushService {

    protected Logger LOGGER = Logger.getLogger(getClass());

    @Autowired(required = false)
    private SimpMessagingTemplate webSocketClientMessagingTemplate;

    @Override
    public void pushToSubscribedClients(String destination, String content) {
        LOGGER.info(String.format("AbstractPushService.pushToSubscribedClients - sending to destination %s - %s",
                destination, content));

        ResponsePayload responsePayload = ResponsePayload
                .builder()
                .jsonContent(content)
                .build();

        ResponseMessage responseMessage = ResponseMessage
                .builder()
                .payload(responsePayload)
                .build();

        webSocketClientMessagingTemplate.convertAndSend(destination,
                responseMessage);
    }
}
