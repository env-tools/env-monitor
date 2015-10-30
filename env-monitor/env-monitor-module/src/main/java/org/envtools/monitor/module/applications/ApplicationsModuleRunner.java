package org.envtools.monitor.module.applications;

import org.apache.log4j.Logger;
import org.envtools.monitor.model.applications.ApplicationsModuleProvider;
import org.envtools.monitor.model.messaging.DataRequestMessage;
import org.envtools.monitor.model.messaging.RequestedDataMessage;
import org.envtools.monitor.model.messaging.RequestedDataPayload;
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
    SubscribableChannel applicationsModuleDataRequestChannel;

    @Resource(name = "core.channel")
    MessageChannel dataPushChannel;

    @Resource(name = "applications.provider")
    ApplicationsModuleProvider applicationsModuleProvider;

    @Autowired
    private Serializer serializer;

    private MessageHandler incomingMessageHandler = (message) -> handleDataRequestForModule((DataRequestMessage) message.getPayload());

    private AtomicLong responseIdentifier = new AtomicLong(0);

    @PostConstruct
    public void init() {
        LOGGER.info("ApplicationsModuleRunner.init - Initializing ApplicationsModuleRunner...");

        applicationsModuleDataRequestChannel.subscribe(incomingMessageHandler);
        applicationsModuleProvider.initialize(this::onModelUpdate);


        LOGGER.info("ApplicationsModuleRunner.init - ApplicationsModuleRunner has been initialized");
    }

    private void onModelUpdate() {
        RequestedDataMessage pushedData = new RequestedDataMessage(String.valueOf(responseIdentifier.incrementAndGet()),
                null, //Session id - N/A
                ModuleConstants.APPLICATIONS_MODULE_ID,
                null, //User ID - N/A
                new RequestedDataPayload(
                        null,
                        serializer.serialize(applicationsModuleProvider.getApplicationsData())
                        ));
        dataPushChannel.send(new GenericMessage<RequestedDataMessage>(pushedData));
    }

    private void handleDataRequestForModule(DataRequestMessage dataRequestMessage) {
        LOGGER.info("ApplicationsModuleRunner.handleDataRequestForModule - dataRequestMessage : " + dataRequestMessage);
        RequestedDataMessage response = new RequestedDataMessage(dataRequestMessage.getRequestId(), dataRequestMessage.getSessionId(),
                dataRequestMessage.getTargetModuleId(), dataRequestMessage.getUsername(),
                new RequestedDataPayload(
                        dataRequestMessage.getPayload(),
                        "Application module processed this message at " + LocalDateTime.now()));
        dataPushChannel.send(new GenericMessage<RequestedDataMessage>(response));
    }



}
