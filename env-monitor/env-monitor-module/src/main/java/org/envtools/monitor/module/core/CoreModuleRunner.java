package org.envtools.monitor.module.core;

import org.apache.log4j.Logger;
import org.envtools.monitor.model.messaging.RequestedDataMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.SubscribableChannel;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created: 10/16/15 9:38 PM
 *
 * @author Yury Yakovlev
 */
public class CoreModuleRunner implements Runnable {
    private static final Logger LOGGER = Logger.getLogger(CoreModuleRunner.class);

    @Autowired
    private SimpMessagingTemplate webSocketClientMessagingTemplate;

    /**
     * This channel accepts the requested data from all modules
     */
    @Resource(name = "core.channel")
    SubscribableChannel requestedDataChannel;

    private ExecutorService threadPool = Executors.newCachedThreadPool();
    private MessageHandler incomingMessageHandler = (message) -> handleRequestedData((RequestedDataMessage) message.getPayload());

    @PostConstruct
    public void init() {
        LOGGER.info("CoreModuleRunner.init - CoreModuleRunner has been initialized,  webSocketClientMessagingTemplate = " + webSocketClientMessagingTemplate);
        requestedDataChannel.subscribe(incomingMessageHandler);
        //threadPool.submit(this);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                Thread.sleep(10000);
                LOGGER.info("CoreModuleRunner.run - core module is up and running...");
            } catch (InterruptedException ex) {
                LOGGER.info("CoreModuleRunner.run - Thread was interrupted");
                break;
            }
        }
    }

    private void handleRequestedData(RequestedDataMessage requestedDataMessage) {
        LOGGER.info("CoreModuleRunner.handleRequestedData - requestedDataMessage : " + requestedDataMessage);
        String user = requestedDataMessage.getSessionId();
        String responseWebSocketDestination = "/topic/moduleresponse";
        String pushedWebSocketDestination = "/topic/modulepush";

        if (user != null) {
            webSocketClientMessagingTemplate.convertAndSendToUser(user, responseWebSocketDestination, requestedDataMessage,
                    createUniqueSessionDestinationHeaders(user));
        } else {
            //TODO manage subscriptions
            webSocketClientMessagingTemplate.convertAndSend(pushedWebSocketDestination, requestedDataMessage);
        }
    }

    @PreDestroy
    public void destroy() {
        requestedDataChannel.unsubscribe(incomingMessageHandler);
        threadPool.shutdownNow();
    }

    private MessageHeaders createUniqueSessionDestinationHeaders(String sessionId) {
        SimpMessageHeaderAccessor headerAccessor = SimpMessageHeaderAccessor.create(SimpMessageType.MESSAGE);
        headerAccessor.setSessionId(sessionId);
        headerAccessor.setLeaveMutable(true);
        return headerAccessor.getMessageHeaders();
    }
}


