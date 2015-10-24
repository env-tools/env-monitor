package org.envtools.monitor.common.messaging.broker.simple;

import org.envtools.monitor.common.messaging.broker.simple.message.SimpleMessage;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * Created: 10/23/15 8:44 PM
 *
 * @author Yury Yakovlev
 */
public class BrokerServer {

    private BlockingQueue<SimpleMessage> queue = new LinkedBlockingDeque<>();

    private ExecutorService threadPool = Executors.newCachedThreadPool();

    public void run() {

    }

    public static void main(String[] args) {

    }

}
