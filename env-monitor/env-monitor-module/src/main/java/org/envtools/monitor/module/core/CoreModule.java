package org.envtools.monitor.module.core;

import com.google.common.collect.Maps;
import org.apache.log4j.Logger;
import org.envtools.monitor.model.messaging.ResponseMessage;
import org.envtools.monitor.module.Module;
import org.envtools.monitor.module.ModuleConstants;
import org.envtools.monitor.module.core.cache.ApplicationsDataPushService;
import org.envtools.monitor.module.core.cache.ApplicationsModuleStorageService;
import org.envtools.monitor.module.core.selection.DestinationUtil;
import org.envtools.monitor.module.core.subscription.SubscriptionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.SubscribableChannel;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageType;

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
public class CoreModule implements Module {

    private static final Logger LOGGER = Logger.getLogger(CoreModule.class);

    /**
     * This service is responsible for pushing applications module data from cache/storage to clients
     */
    @Autowired
    ApplicationsDataPushService applicationsDataPushService;

    @Autowired
    ApplicationsModuleStorageService applicationsModuleStorageService;

    @Autowired
    SubscriptionManager subscriptionManager;
    /**
     * This channel accepts the requested data from all modules
     */
    @Resource(name = "core.channel")
    SubscribableChannel coreModuleChannel;

    private ExecutorService threadPool = Executors.newCachedThreadPool();
    private MessageHandler incomingMessageHandler = (message) -> handlePluggableModuleResponse((ResponseMessage) message.getPayload());

    private Map<String, String> contentBySelector = Maps.newConcurrentMap();

    @PostConstruct
    public void init() {
        LOGGER.info("CoreModule.init - CoreModule has been initialized");
        coreModuleChannel.subscribe(incomingMessageHandler);
    }

    private void handlePluggableModuleResponse(ResponseMessage responseMessage) {

        String user = responseMessage.getSessionId();
        String responseWebSocketDestination = "/topic/moduleresponse";

//TODO is this branch of logic needed now?
//        if (user != null) {
//            webSocketClientMessagingTemplate.convertAndSendToUser(user, responseWebSocketDestination, responseMessage,
//                    createUniqueSessionDestinationHeaders(user));
//        } else {
            String newContent = responseMessage.getPayload().getJsonContent();
            applicationsModuleStorageService.storeFull(newContent);

            for (String subscribedDestination : subscriptionManager.getSubscribedDestinations()) {

                String newContentPart = applicationsModuleStorageService.extractPartBySelector(
                        DestinationUtil.extractSelector(subscribedDestination));

                //TODO handle synchronization
                if (!contentBySelector.containsKey(subscribedDestination) ||
                        !newContentPart.equals(contentBySelector.get(subscribedDestination))) {
                    applicationsDataPushService.pushToSubscribedClient(subscribedDestination, newContentPart);
                }
            }
//        }
    }

    @PreDestroy
    public void destroy() {
        coreModuleChannel.unsubscribe(incomingMessageHandler);
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

