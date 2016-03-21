package org.envtools.monitor.module.core;

import com.fasterxml.jackson.annotation.JsonRawValue;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.Map;

public class JsonConversionTest extends Assert {

    public static String INPUT_JSON = "{\"a\":7,\"data\":{\"x\":\"3\",\"y\":\"2\"}}";

    @Test
    public void testStringSerializationJackson() throws IOException {
        //TODO implement
        int a = 7;
        String data = "{\"x\":\"3\",\"y\":\"2\"}";
        TargetClass targetClass = new TargetClass();
        targetClass.setA(a);
        targetClass.setDataJson(data);

        ObjectMapper objectMapper = new ObjectMapper();
        String output = objectMapper.writeValueAsString(targetClass);
        System.out.println(output);
        assertEquals(INPUT_JSON, output);

        TargetClass deserialized = objectMapper.readValue(output, TargetClass.class);
        assertEquals(a, deserialized.a);
        assertEquals(data, deserialized.data);
    }

    public static class TargetClass {
        private int a;

        private String data;

        public int getA() {
            return a;
        }

        public void setA(int a) {
            this.a = a;
        }

        @JsonRawValue
        public String getData() {
            return data;
        }

        public void setDataJson(String data) {
            this.data = data;
        }

        public void setData(Object data) {
            JSONObject jsonObject = new JSONObject((Map<String, Object>) data);
            this.data = jsonObject.toString();
        }

        @Override
        public String toString() {
            return new ToStringBuilder(this)
                    .append("data", data)
                    .toString();
        }
    }
}
