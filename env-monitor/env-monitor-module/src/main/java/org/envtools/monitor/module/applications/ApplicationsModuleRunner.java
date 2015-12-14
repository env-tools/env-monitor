package org.envtools.monitor.module.applications;

import org.apache.log4j.Logger;
import org.envtools.monitor.model.applications.ApplicationsModuleProvider;
import org.envtools.monitor.model.messaging.RequestMessage;
import org.envtools.monitor.model.messaging.ResponseMessage;
import org.envtools.monitor.model.messaging.ResponsePayload;
import org.envtools.monitor.module.ModuleConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.SubscribableChannel;
import org.springframework.messaging.support.GenericMessage;

import org.envtools.monitor.common.serialization.*;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created: 10/16/15 10:35 PM
 *
 * @author Yury Yakovlev
 */
public class ApplicationsModuleRunner {
    private static final Logger LOGGER = Logger.getLogger(ApplicationsModuleRunner.class);

    /**
     * This is incoming channel for APPLICATIONS module
     */
    @Resource(name = "applications.channel")
    SubscribableChannel applicationsModuleChannel;

    @Resource(name = "core.channel")
    MessageChannel coreModuleChannel;

    @Resource(name = "applications.provider")
    ApplicationsModuleProvider applicationsModuleProvider;

    @Autowired
    private Serializer serializer;

    private MessageHandler incomingMessageHandler = (message) -> handleDataRequestForModule((RequestMessage) message.getPayload());

    private AtomicLong responseIdentifier = new AtomicLong(0);

    @PostConstruct
    public void init() {
        LOGGER.info("ApplicationsModuleRunner.init - Initializing ApplicationsModuleRunner...");

        applicationsModuleChannel.subscribe(incomingMessageHandler);
        applicationsModuleProvider.initialize(this::onModelUpdate);

        LOGGER.info("ApplicationsModuleRunner.init - ApplicationsModuleRunner has been initialized");
    }

    private void onModelUpdate() {

        ResponseMessage updatedModelMessage = createModelUpdateMessage();

        coreModuleChannel.send(new GenericMessage<ResponseMessage>(updatedModelMessage));

    }

    private ResponseMessage createModelUpdateMessage() {
        ResponseMessage updatedModelMessage = new ResponseMessage(String.valueOf(responseIdentifier.incrementAndGet()),
                null, //Session id - N/A
                ModuleConstants.APPLICATIONS_MODULE_ID,
                null, //User ID - N/A
                new ResponsePayload(
                        null,
                        serializer.serialize(applicationsModuleProvider.getApplicationsData())
                ));
        return updatedModelMessage;
    }

    private void handleDataRequestForModule(RequestMessage requestMessage) {
        LOGGER.info("ApplicationsModuleRunner.handleDataRequestForModule - requestMessage : " + requestMessage);
        ResponseMessage response = new ResponseMessage(requestMessage.getRequestId(), requestMessage.getSessionId(),
                requestMessage.getTargetModuleId(), requestMessage.getUsername(),
                new ResponsePayload(
                        requestMessage.getPayload(),
                        "\"Application module processed this message at " + LocalDateTime.now() + "\""));
        coreModuleChannel.send(new GenericMessage<ResponseMessage>(response));
    }


}
