package org.envtools.monitor.module.applications;

import com.google.common.collect.Maps;
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
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Map;
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
        return
                ResponseMessage
                        .builder()
                        .requestId(String.valueOf(responseIdentifier.incrementAndGet()))
                        .targetModuleId(ModuleConstants.APPLICATIONS_MODULE_ID)
                        .payload(serializer.serialize(applicationsModuleProvider.getApplicationsData()))
                        .build();
    }

    @Override
    protected <T> void processPayload(T payload, RequestMessage requestMessage) {
        //Do nothing
    }

    @Override
    protected Map<String, Class<?>> getPayloadTypes() {
        return Maps.newHashMap();
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
