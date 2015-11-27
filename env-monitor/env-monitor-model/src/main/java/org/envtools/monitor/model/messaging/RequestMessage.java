package org.envtools.monitor.model.messaging;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * Created: 9/20/15 12:56 AM
 *
 * @author Yury Yakovlev
 *
 * This class represents a request (subscription) for some data, addressed to a specific module
 */
public class RequestMessage {

    private String requestId;
    private String sessionId;
    private String targetModuleId;
    private String username;
    private RequestPayload payload;

    //TODO implement builder classes

    //For Jackson
    public RequestMessage() {
    }

    public RequestMessage(String requestId, String sessionId, String targetModuleId, String username, RequestPayload payload) {
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

    public RequestPayload getPayload() {
        return payload;
    }

    public void setPayload(RequestPayload payload) {
        this.payload = payload;
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
