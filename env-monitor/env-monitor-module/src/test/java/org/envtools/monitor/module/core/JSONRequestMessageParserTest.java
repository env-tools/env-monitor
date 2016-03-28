package org.envtools.monitor.module.core;

import org.envtools.monitor.model.messaging.RequestMessage;
import org.envtools.monitor.model.messaging.RequestPayload;
import org.envtools.monitor.model.updates.DataOperation;
import org.envtools.monitor.model.updates.DataOperationType;
import org.envtools.monitor.module.core.json.JSONRequestMessageParser;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by jesa on 26.03.2016.
 */
public class JSONRequestMessageParserTest extends Assert {

    public static String INPUT_REQUEST = "{\"requestId\":\"a\",\"destination\":\"b\",\"sessionId\":\"c\",\"targetModuleId\":\"d\",\"username\":\"e\",\"payload\":{\"payloadType\":\"f\",\"content\":{\"dataOperation\":{\"type\":\"3\",\"y\":\"2\"}}}}",
            INPUT_DATAOPERATON = "{\"type\":\"CREATE\",\"entity\":\"Category\",\"fields\":{\"title\":\"newTitle\",\"owner\":\"Owner1\",\"parentCategoryID\":5}}";

    @Test
    public void testRequestMessageParser() throws IOException {
        RequestMessage requestMessage = JSONRequestMessageParser.parseRequestMessage(INPUT_REQUEST);
        RequestMessage resultMessage = new RequestMessage("a", "b", "c", "d", "e", new RequestPayload("{\"dataOperation\":{\"y\":\"2\",\"type\":\"3\"}}", "f"));
        assertEquals(requestMessage.toString(), resultMessage.toString());
    }

    @Test
    public void testPayloadParser() throws IOException {
        DataOperation dataOperation = JSONRequestMessageParser.parsePayload(INPUT_DATAOPERATON, DataOperation.class);
        Map<String, String> fields = new LinkedHashMap<>();
        fields.put("title", "newTitle");
        fields.put("owner", "Owner1");
        fields.put("parentCategoryID", "5");
        DataOperation resultDataOperation = new DataOperation(DataOperationType.CREATE, "Category", fields);
        assertEquals(dataOperation.toString(), resultDataOperation.toString());
    }
}
