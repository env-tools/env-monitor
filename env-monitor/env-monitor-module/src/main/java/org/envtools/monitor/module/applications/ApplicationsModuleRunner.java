package org.envtools.monitor.module.applications;

import com.sun.management.OperatingSystemMXBean;
import org.apache.log4j.Logger;
import org.envtools.monitor.model.DataRequestMessage;
import org.envtools.monitor.model.RequestedDataMessage;
import org.envtools.monitor.model.RequestedDataPayload;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.SubscribableChannel;
import org.springframework.messaging.support.GenericMessage;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.lang.management.ManagementFactory;
import java.time.LocalDateTime;

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

    private MessageHandler incomingMessageHandler = (message) -> handleDataRequestForModule((DataRequestMessage) message.getPayload());

    @PostConstruct
    public void init() {
        LOGGER.info("ApplicationsModuleRunner.init - ApplicationsModuleRunner has been initialized");
        applicationsModuleDataRequestChannel.subscribe(incomingMessageHandler);
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

    private double getCpuLoad() {
        OperatingSystemMXBean osBean =
                (com.sun.management.OperatingSystemMXBean) ManagementFactory.
                        getPlatformMXBeans(OperatingSystemMXBean.class);
        return osBean.getProcessCpuLoad();
    }

}
