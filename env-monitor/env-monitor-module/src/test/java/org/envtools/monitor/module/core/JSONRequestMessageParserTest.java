package org.envtools.monitor.module.core;

import org.envtools.monitor.model.messaging.RequestMessage;
import org.envtools.monitor.model.updates.DataOperation;
import org.envtools.monitor.module.core.json.JSONRequestMessageParser;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

/**
 * Created by jesa on 26.03.2016.
 */
public class JSONRequestMessageParserTest extends Assert {

    public static String INPUT_REQUEST = "{\"requestId\":\"a\",\"sessionId\":\"b\",\"targetModuleId\":\"c\",\"username\":\"d\",\"payload\":{\"payloadType\":\"e\",\"content\":{\"dataOperation\":{\"type\":\"3\",\"y\":\"2\"}}}}",
            INPUT_DATAOPERATON = "{\"type\":\"CREATE\",\"entity\":\"Category\",\"fields\":{\"title\":\"newTitle\",\"owner\":\"Owner1\",\"parentCategoryID\":5}}";

    @Test
    public void testRequestMessageParser() throws IOException {
        RequestMessage requestMessage = JSONRequestMessageParser.parseRequestMessage(INPUT_REQUEST);
        System.out.println(requestMessage);
    }

    @Test
    public void testPayloadParser() throws IOException {
        DataOperation dataOperation = JSONRequestMessageParser.parsePayload(INPUT_DATAOPERATON, DataOperation.class);
        System.out.println(dataOperation);
    }
}
