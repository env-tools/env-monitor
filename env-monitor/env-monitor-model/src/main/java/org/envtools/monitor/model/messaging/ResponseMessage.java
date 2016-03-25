package org.envtools.monitor.model.messaging;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * Created: 10/16/15 10:07 PM
 *
 * @author Yury Yakovlev
 *  This class represents a one-time or periodic data pushed by a module to the data requestor (subscriber)
 *
 */
public class ResponseMessage {
    private String requestId;
    private String sessionId;
    private String targetModuleId;
    private String username;
    private ResponsePayload payload;

    public ResponseMessage() {
    }

    public ResponseMessage(String requestId, String sessionId, String targetModuleId, String username, ResponsePayload payload) {
        this.requestId = requestId;
        this.sessionId = sessionId;
        this.targetModuleId = targetModuleId;
        this.username = username;
        this.payload = payload;
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
        private String requestId;
        private String sessionId;
        private String targetModuleId;
        private String username;
        private ResponsePayload payload;

        public Builder requestMetaData(RequestMessage requestMessage) {
            return
                    requestId(requestMessage.getRequestId())
                    .sessionId(requestMessage.getSessionId())
                    .targetModuleId(requestMessage.getTargetModuleId());
        }

        public Builder requestId(String requestId) {
            this.requestId = requestId;
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

        public Builder payload(ResponsePayload payload) {
            this.payload = payload;
            return this;
        }

        public ResponseMessage build() {
            return new ResponseMessage(requestId, sessionId, targetModuleId, username, payload);
        }
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).
                append("requestId", requestId).
                append("sessionId", sessionId).
                append("targetModuleId", targetModuleId).
                append("username", username).
                append("payload", payload).
                toString();
    }
}
