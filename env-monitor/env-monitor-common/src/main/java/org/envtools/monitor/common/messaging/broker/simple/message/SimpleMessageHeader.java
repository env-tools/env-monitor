package org.envtools.monitor.common.messaging.broker.simple.message;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import javax.xml.bind.annotation.*;

/**
 * Created: 10/23/15 9:34 PM
 *
 * @author Yury Yakovlev
 */
@XmlRootElement(name = "messageHeader")
@XmlAccessorType(value = XmlAccessType.NONE)
@XmlType(name = "SimpleMessageHeader", propOrder = {"messageType", "destination", "payloadSize"})
public class SimpleMessageHeader {

    private SimpleMessageTypeEnum messageType;
    private String destination;
    private Integer payloadSize;

    public SimpleMessageHeader() {
    }

    @XmlElement(name = "messageType", required = true)
    public SimpleMessageTypeEnum getMessageType() {
        return messageType;
    }

    public void setMessageType(SimpleMessageTypeEnum messageType) {
        this.messageType = messageType;
    }

    @XmlElement(name = "destination", required = true)
    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    @XmlElement(name = "payloadSize", required = true)
    public Integer getPayloadSize() {
        return payloadSize;
    }

    public void setPayloadSize(Integer payloadSize) {
        this.payloadSize = payloadSize;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).
                append("messageType", messageType).
                append("destination", destination).
                append("payloadSize", payloadSize).
                toString();
    }
}
