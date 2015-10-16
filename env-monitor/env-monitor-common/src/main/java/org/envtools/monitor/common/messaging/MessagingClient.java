package org.envtools.monitor.common.messaging;

import java.util.Properties;

public interface MessagingClient {

    Producer createProducer(Properties properties);

    Consumer createConsumer(Properties properties);

}
