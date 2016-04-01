package org.envtools.monitor.model.messaging;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import com.fasterxml.jackson.annotation.JsonRawValue;
import org.envtools.monitor.model.messaging.content.AbstractContent;

import java.io.Serializable;

/**
 * Created: 10/16/15 10:09 PM
 *
 * @author Yury Yakovlev
 */
public class ResponsePayload implements Serializable{

    @JsonRawValue
    private String jsonContent;

    private AbstractContent content;


    public ResponsePayload() {
    }

    public ResponsePayload(String jsonContent) {
        this.jsonContent = jsonContent;
    }

    public ResponsePayload(AbstractContent content) {
        this.content = content;
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
        private AbstractContent<?> abstractContent;

        private Builder() {
        }

        public Builder jsonContent(String jsonContent) {
            this.jsonContent = jsonContent;
            return this;
        }

        public Builder abstractContent(AbstractContent content) {
             this.abstractContent = content;
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
                append("content", content).
                toString();
    }
}
