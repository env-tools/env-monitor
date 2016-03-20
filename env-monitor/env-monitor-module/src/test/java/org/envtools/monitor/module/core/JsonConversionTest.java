package org.envtools.monitor.module.core;

import org.junit.Test;

public class JsonConversionTest {

    public static String INPUT_JSON = "{\"a\":\"7\", \"data\": { \"x\":\"3\",\"y\":\"2\" }} ";

    @Test
    public void testStringSerializationJackson() {
           //TODO implement
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

        public String getData() {
            return data;
        }

        public void setData(String data) {
            this.data = data;
        }
    }
}
