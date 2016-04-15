package org.envtools.monitor.module;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.envtools.monitor.model.messaging.RequestMessage;
import org.envtools.monitor.model.messaging.RequestPayload;
import org.envtools.monitor.model.messaging.ResponseMessage;
import org.envtools.monitor.module.exception.MessageFormatException;
import org.envtools.monitor.module.querylibrary.services.CoreModuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.SubscribableChannel;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.Map;

/**
 * Created: 12.03.16 19:18
 *
 * @author Yury Yakovlev
 */


public abstract class AbstractPluggableModule implements Module {

    private static final Logger LOGGER = Logger.getLogger(AbstractPluggableModule.class);

    //@Resource(name = "core.channel")
    //MessageChannel coreModuleChannel;

    private MessageHandler incomingMessageHandler = (message) -> handleIncomingMessage((RequestMessage) message.getPayload());

    @PostConstruct
    public void init() throws Exception {
        getModuleChannel().subscribe(incomingMessageHandler);
    }

    public void handleIncomingMessage(RequestMessage requestMessage) {

        RequestPayload requestPayload = requestMessage.getPayload();
        String payloadType = requestPayload.getPayloadType();

        if (payloadType == null) {
            LOGGER.error("QueryLibraryModule.handleIncomingMessage - payloadType not specified, ignoring message" + requestMessage);
            return;
        }

        Map<String, Class<?>> payloadTypes = getPayloadTypes();
        Class<?> clazz = payloadTypes.get(payloadType);

        if (clazz == null) {
            LOGGER.error(String.format("AbstractPluggableModule.handleIncomingMessage - payloadType %s unknown. Supported types : %s",
                    payloadType,
                    payloadTypes.keySet()));
            return;
        }

        processPayload(parsePayload(requestPayload.getContent(), clazz), requestMessage);

    }

    protected abstract <T> void processPayload(T payload, RequestMessage requestMessage);


    @Autowired
    CoreModuleService coreModuleService;

    public void sendMessageToCore(ResponseMessage responseMessage) {
        coreModuleService.sendToCore(responseMessage);
    }

    protected abstract SubscribableChannel getModuleChannel();

    protected abstract Map<String, Class<?>> getPayloadTypes();

    protected <T> T parsePayload(String payload, Class<T> clazz) {
        LOGGER.info(String.format("Parse payload %s for %s class", payload, clazz));
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(payload, clazz);
        } catch (IOException e) {
            throw new MessageFormatException(
                    String.format("Invalid payload %s for type %s", payload, clazz));
        }
    }
}
