package org.envtools.monitor.model.messaging;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.io.Serializable;

/**
 * Created: 9/20/15 12:58 AM
 *
 * @author Yury Yakovlev
 */
public class RequestPayload implements Serializable{

    private String payloadType;
    private String content;

    //For Jackson
    public RequestPayload() {
    }

    public RequestPayload(String content, String payloadType) {
        this.content = content;
        this.payloadType = payloadType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPayloadType() {
        return payloadType;
    }

    public void setPayloadType(String payloadType) {
        this.payloadType = payloadType;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String payloadType;
        private String content;

        public Builder payloadType(String payloadType) {
            this.payloadType = payloadType;
            return this;
        }

        public Builder content(String content) {
            this.content = content;
            return this;
        }

        public RequestPayload build() {
            return new RequestPayload(content, payloadType);
        }

    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).
                append("content", content).
                append("payloadType", payloadType).
                toString();
    }
}
