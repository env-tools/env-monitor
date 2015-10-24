package org.envtools.monitor.common.messaging.broker.simple.message;

/**
 * Created: 10/23/15 10:02 PM
 *
 * @author Yury Yakovlev
 */
public class SimpleMessage {

    private final SimpleMessageHeader header;
    private final byte[] payload;

    public SimpleMessage(SimpleMessageHeader header, byte[] payload) {
        this.header = header;
        this.payload = payload;
    }

    public SimpleMessageHeader getHeader() {
        return header;
    }

    public byte[] getPayload() {
        return payload;
    }

}
