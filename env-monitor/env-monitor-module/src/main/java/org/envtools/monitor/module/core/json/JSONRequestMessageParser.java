package org.envtools.monitor.module.core.json;

import org.codehaus.jackson.map.ObjectMapper;
import org.envtools.monitor.model.messaging.RequestMessage;

import java.io.IOException;

/**
 * Created by jesa on 26.03.2016.
 */
public class JSONRequestMessageParser {
    public static RequestMessage parseRequestMessage(String requestMessageJson) throws IOException {
        RequestMessage requestMessage;
        ObjectMapper objectMapper = new ObjectMapper();
        requestMessage = objectMapper.readValue(requestMessageJson, RequestMessage.class);
        return requestMessage;
    }

    public static <T> T parsePayload(String payloadJson, Class<T> type) throws IOException {
        T dataOperation;
        ObjectMapper objectMapper = new ObjectMapper();
        dataOperation = objectMapper.readValue(payloadJson, type);
        return type.cast(dataOperation);
    }
}
