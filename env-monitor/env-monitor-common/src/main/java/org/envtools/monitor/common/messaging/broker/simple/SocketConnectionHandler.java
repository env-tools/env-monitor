package org.envtools.monitor.common.messaging.broker.simple;

import org.apache.log4j.Logger;
import org.envtools.monitor.common.jaxb.JaxbHelper;
import org.envtools.monitor.common.messaging.Consumer;
import org.envtools.monitor.common.messaging.Producer;
import org.envtools.monitor.common.messaging.broker.simple.message.SimpleMessage;
import org.envtools.monitor.common.messaging.broker.simple.message.SimpleMessageHeader;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;

/**
 * Created: 10/23/15 8:53 PM
 *
 * @author Yury Yakovlev
 */
public class SocketConnectionHandler implements Runnable {

    private static final Logger LOGGER = Logger.getLogger(SocketConnectionHandler.class);

    private ConnectionTypeEnum connectionType;

    private Socket socket;
    private BlockingQueue queue;

    public SocketConnectionHandler(Socket socket, BlockingQueue<SimpleMessage> queue) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            LOGGER.info("SocketConnectionHandler.run - Received a connection  from " + socket.getInetAddress());

            // Get input stream
            DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
            String connectionTypeString = dataInputStream.readUTF();
            switch (connectionTypeString) {
                case Producer.CONNECTION_TYPE:
                    connectionType = ConnectionTypeEnum.PRODUCER;
                    break;
                case Consumer.CONNECTION_TYPE:
                    connectionType = ConnectionTypeEnum.CONSUMER;
                    break;
                default:
                    terminateConnection();
                    return;

            }

            while (true) {
                String headerString = dataInputStream.readUTF();
                SimpleMessageHeader header = JaxbHelper.unmarshallFromString(headerString, SimpleMessageHeader.class);
                switch (header.getMessageType()) {
                    case CLOSE:
                        terminateConnection();
                        return;
                    case MESSAGE:
                        if (!ConnectionTypeEnum.PRODUCER.equals(connectionType)) {
                            LOGGER.warn("SocketConnectionHandler.run - only producers may send messagase, ignoring " + header);
                            readPayload(dataInputStream, header.getPayloadSize());
                            continue;
                        }

                        byte[] payload = readPayload(dataInputStream, header.getPayloadSize());

                }
            }

        } catch (Exception e) {
            LOGGER.info(String.format("SocketConnectionHandler.run - exception in interaction with %s", socket.getInetAddress()), e);
            terminateConnection();
        }
    }

    private byte[] readPayload(DataInputStream dataInputStream, int payloadSize) {

        if (payloadSize == 0) {
            return new byte[0];
        }

        byte[] result = new byte[payloadSize];
        try {
            dataInputStream.read(result);
        } catch (IOException e) {
            LOGGER.error("SocketConnectionHandler.readPayload - could not read payload", e);
            return new byte[0];
        }
        return result;
    }

    private void terminateConnection() {
        if (socket != null) {
            try {
                socket.close();
                LOGGER.info("SocketConnectionHandler.terminateConnection - connection to " + socket.getInetAddress() + " closed.");
            } catch (IOException e) {
                LOGGER.info("SocketConnectionHandler.terminateConnection - could not close connection to " + socket.getInetAddress());
            }
        }
    }
}
