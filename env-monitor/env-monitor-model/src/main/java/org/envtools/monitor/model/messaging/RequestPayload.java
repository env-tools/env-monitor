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
    private String content;

    //For Jackson
    public RequestPayload() {
    }

    public RequestPayload(String content) {
        this.content = content;
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
                append("content", content).
                toString();
    }
}
