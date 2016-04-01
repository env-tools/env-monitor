package org.envtools.monitor.model.messaging;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.envtools.monitor.model.messaging.content.AbstractContent;

/**
 * Created: 10/16/15 10:07 PM
 *
 * @author Yury Yakovlev
 *  This class represents a one-time or periodic data pushed by a module to the data requestor (subscriber)
 *
 */
public class ResponseMessage {
    private ResponseType type;
    private String requestId;
    private String destination;
    private String sessionId;
    private String targetModuleId;
    private String username;
    private ResponsePayload payload;

    public ResponseMessage() {
    }

    public ResponseMessage(
            ResponseType type,
            String requestId,
            String destination,
            String sessionId,
            String targetModuleId,
            String username,
            ResponsePayload payload) {
        this.type = type;
        this.requestId = requestId;
        this.destination = destination;
        this.sessionId = sessionId;
        this.targetModuleId = targetModuleId;
        this.username = username;
        this.payload = payload;
    }

    public ResponseType getType() {
        return type;
    }

    public void setType(ResponseType type) {
        this.type = type;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getTargetModuleId() {
        return targetModuleId;
    }

    public void setTargetModuleId(String targetModuleId) {
        this.targetModuleId = targetModuleId;
    }

    public String getUsername() {
        return username;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public ResponsePayload getPayload() {
        return payload;
    }

    public void setPayload(ResponsePayload payload) {
        this.payload = payload;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private ResponseType type;
        private String requestId;
        private String destination;
        private String sessionId;
        private String targetModuleId;
        private String username;
        private ResponsePayload payload;

        public Builder type(ResponseType type) {
            this.type = type;
            return this;
        }

        public Builder requestMetaData(RequestMessage requestMessage) {
            return
                    requestId(requestMessage.getRequestId())
                    .sessionId(requestMessage.getSessionId())
                    .destination(requestMessage.getDestination())
                    .targetModuleId(requestMessage.getTargetModuleId());
        }

        public Builder requestId(String requestId) {
            this.requestId = requestId;
            return this;
        }

        public Builder destination(String destination) {
            this.destination = destination;
            return this;
        }

        public Builder sessionId(String sessionId) {
            this.sessionId = sessionId;
            return this;
        }

        public Builder targetModuleId(String targetModuleId) {
            this.targetModuleId = targetModuleId;
            return this;
        }

        public Builder username(String username) {
            this.username = username;
            return this;
        }

        public Builder payload(String payload) {
            this.payload = new ResponsePayload(payload);
            return this;
        }

        public Builder payload(AbstractContent c) {
            this.payload = new ResponsePayload(c);
            return this;
        }

        public Builder payload(ResponsePayload payload) {
            this.payload = payload;
            return this;
        }

        public ResponseMessage build() {
            return new ResponseMessage(type, requestId, destination, sessionId, targetModuleId, username, payload);
        }
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).
                append("type", type).
                append("requestId", requestId).
                append("destination", destination).
                append("sessionId", sessionId).
                append("targetModuleId", targetModuleId).
                append("username", username).
                append("payload", payload).
                toString();
    }
}
