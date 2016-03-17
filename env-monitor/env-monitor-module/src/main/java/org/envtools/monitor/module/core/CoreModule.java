package org.envtools.monitor.module.core;

import com.google.common.collect.Maps;
import org.apache.log4j.Logger;
import org.envtools.monitor.model.messaging.ResponseMessage;
import org.envtools.monitor.model.messaging.ResponsePayload;
import org.envtools.monitor.module.Module;
import org.envtools.monitor.module.ModuleConstants;
import org.envtools.monitor.module.core.selection.DestinationUtil;
import org.envtools.monitor.module.core.subscription.SubscriptionManager;
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
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created: 10/16/15 9:38 PM
 *
 * @author Yury Yakovlev
 */
public class CoreModule implements Module, Runnable {
    private static final Logger LOGGER = Logger.getLogger(CoreModule.class);

    @Autowired
    private SimpMessagingTemplate webSocketClientMessagingTemplate;

    @Autowired
    ApplicationsModuleStorageService applicationsModuleStorageService;

    @Autowired
    SubscriptionManager subscriptionManager;
    /**
     * This channel accepts the requested data from all modules
     */
    @Resource(name = "core.channel")
    SubscribableChannel requestedDataChannel;

    private ExecutorService threadPool = Executors.newCachedThreadPool();
    private MessageHandler incomingMessageHandler = (message) -> handleModuleResponse((ResponseMessage) message.getPayload());

    private Map<String, String> contentBySelector = Maps.newConcurrentMap();

    @PostConstruct
    public void init() {
        LOGGER.info("CoreModule.init - CoreModule has been initialized,  webSocketClientMessagingTemplate = " + webSocketClientMessagingTemplate);
        requestedDataChannel.subscribe(incomingMessageHandler);
        //threadPool.submit(this);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                Thread.sleep(10000);
                LOGGER.info("CoreModule.run - core module is up and running...");
            } catch (InterruptedException ex) {
                LOGGER.info("CoreModule.run - Thread was interrupted");
                break;
            }
        }
    }

    private void handleModuleResponse(ResponseMessage responseMessage) {
        //       LOGGER.info("CoreModule.handleModuleResponse - responseMessage : " + responseMessage);
        String user = responseMessage.getSessionId();
        String responseWebSocketDestination = "/topic/moduleresponse";

        if (user != null) {
            webSocketClientMessagingTemplate.convertAndSendToUser(user, responseWebSocketDestination, responseMessage,
                    createUniqueSessionDestinationHeaders(user));
        } else {
            String newContent = responseMessage.getPayload().getJsonContent();
            applicationsModuleStorageService.store(newContent);

            for (String subscribedDestination : subscriptionManager.getSubscribedDestinations()) {

                String newContentPart = applicationsModuleStorageService.extractSerializedPartBySelector(
                        DestinationUtil.extractSelector(subscribedDestination));

                //TODO handle synchronization
                if (!contentBySelector.containsKey(subscribedDestination) ||
                        !newContentPart.equals(contentBySelector.get(subscribedDestination))) {
                    ResponseMessage subscriberResponseMessage = new ResponseMessage.Builder()
                            .payload(new ResponsePayload(null,
                                    newContentPart
                            ))
                            .build();

                    webSocketClientMessagingTemplate.convertAndSend(subscribedDestination,
                            subscriberResponseMessage);
                }
            }
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

    @Override
    public String getIdentifier() {
        return ModuleConstants.CORE_MODULE_ID;
    }
}


