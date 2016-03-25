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

    @JsonRawValue
    private String jsonContent;

    public ResponsePayload() {
    }

    public ResponsePayload(String jsonContent) {
        this.jsonContent = jsonContent;
    }

    public String getJsonContent() {
        return jsonContent;
    }

    public void setJsonContent(String jsonContent) {
        this.jsonContent = jsonContent;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String jsonContent;

        private Builder() {
        }

        public Builder jsonContent(String jsonContent) {
            this.jsonContent = jsonContent;
            return this;
        }

        public ResponsePayload build() {
            return new ResponsePayload(jsonContent);
        }
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).
                append("jsonContent", jsonContent).
                toString();
    }
}
