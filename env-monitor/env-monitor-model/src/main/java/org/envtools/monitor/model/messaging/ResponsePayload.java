package org.envtools.monitor.model.messaging;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.io.Serializable;

/**
 * Created: 10/16/15 10:09 PM
 *
 * @author Yury Yakovlev
 */
public class ResponsePayload implements Serializable{
    private RequestPayload requestPayload;
    private String content;

    public ResponsePayload() {
    }

    public ResponsePayload(RequestPayload requestPayload, String content) {
        this.requestPayload = requestPayload;
        this.content = content;
    }

    public RequestPayload getRequestPayload() {
        return requestPayload;
    }

    public void setRequestPayload(RequestPayload requestPayload) {
        this.requestPayload = requestPayload;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).
                append("requestPayload", requestPayload).
                append("content", content).
                toString();
    }
}
