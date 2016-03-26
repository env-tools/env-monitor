package org.envtools.monitor.module.core.json;

import org.codehaus.jackson.map.ObjectMapper;
import org.envtools.monitor.model.messaging.RequestMessage;
import org.envtools.monitor.model.updates.DataOperation;

import java.io.IOException;

/**
 * Created by jesa on 26.03.2016.
 */
public class JSONRequestMessageParser {
    public static RequestMessage parseRequestMessage(String requestMessageJson) throws IOException {
        RequestMessage requestMessage = new RequestMessage();
        ObjectMapper objectMapper = new ObjectMapper();
        requestMessage = objectMapper.readValue(requestMessageJson, RequestMessage.class);
        return requestMessage;
    }

    public static DataOperation parsePayload(String payloadJson) throws IOException {
        DataOperation dataOperation = new DataOperation();
        ObjectMapper objectMapper = new ObjectMapper();
        dataOperation = objectMapper.readValue(payloadJson, DataOperation.class);
        return dataOperation;
    }
}
