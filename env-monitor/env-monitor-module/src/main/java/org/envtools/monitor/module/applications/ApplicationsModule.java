package org.envtools.monitor.module.applications;

import org.apache.log4j.Logger;
import org.envtools.monitor.model.applications.ApplicationsModuleProvider;
import org.envtools.monitor.model.messaging.RequestMessage;
import org.envtools.monitor.model.messaging.ResponseMessage;
import org.envtools.monitor.model.messaging.ResponsePayload;
import org.envtools.monitor.module.AbstractPluggableModule;
import org.envtools.monitor.module.ModuleConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.SubscribableChannel;

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
public class ApplicationsModule extends AbstractPluggableModule {

    private static final Logger LOGGER = Logger.getLogger(ApplicationsModule.class);

    /**
     * This is incoming channel for APPLICATIONS module
     */
    @Resource(name = "applications.channel")
    SubscribableChannel applicationsModuleChannel;

    @Resource(name = "applications.provider")
    ApplicationsModuleProvider applicationsModuleProvider;

    @Autowired
    private Serializer serializer;

    private AtomicLong responseIdentifier = new AtomicLong(0);

    @PostConstruct
    public void init() {
        LOGGER.info("ApplicationsModule.init - Initializing ApplicationsModule...");
        applicationsModuleProvider.initialize(this::onModelUpdate);
        LOGGER.info("ApplicationsModule.init - ApplicationsModule has been initialized");
    }

    private void onModelUpdate() {
        ResponseMessage updatedModelMessage = createModelUpdateMessage();
        sendMessageToCore(updatedModelMessage);
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

    @Override
    public void handleIncomingMessage(RequestMessage requestMessage) {
        LOGGER.info("ApplicationsModule.handleIncomingMessage - requestMessage : " + requestMessage);
        ResponseMessage response = new ResponseMessage(requestMessage.getRequestId(), requestMessage.getSessionId(),
                requestMessage.getTargetModuleId(), requestMessage.getUsername(),
                new ResponsePayload(
                        requestMessage.getPayload(),
                        "\"Application module processed this message at " + LocalDateTime.now() + "\""));
        sendMessageToCore(response);
    }

    @Override
    protected SubscribableChannel getModuleChannel() {
        return applicationsModuleChannel;
    }

    @Override
    public String getIdentifier() {
        return ModuleConstants.APPLICATIONS_MODULE_ID;
    }
}
