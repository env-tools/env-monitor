package org.envtools.monitor.model.messaging;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import com.fasterxml.jackson.annotation.JsonRawValue;

import java.io.Serializable;

/**
 * Created: 10/16/15 10:09 PM
 *
 * @author Yury Yakovlev
 */
public class ResponsePayload implements Serializable{
    private RequestPayload requestPayload;
    @JsonRawValue
    private String jsonContent;

    public ResponsePayload() {
    }

    public ResponsePayload(RequestPayload requestPayload, String jsonContent) {
        this.requestPayload = requestPayload;
        this.jsonContent = jsonContent;
    }

    public RequestPayload getRequestPayload() {
        return requestPayload;
    }

    public void setRequestPayload(RequestPayload requestPayload) {
        this.requestPayload = requestPayload;
    }

    public String getJsonContent() {
        return jsonContent;
    }

    public void setJsonContent(String jsonContent) {
        this.jsonContent = jsonContent;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).
                append("requestPayload", requestPayload).
                append("jsonContent", jsonContent).
                toString();
    }
}
