package org.envtools.monitor.model.querylibrary.updates;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.time.LocalDateTime;

/**
 * Created by jesa on 12.04.2016.
 */
public class UpdateTrigger {
    public LocalDateTime timestamp;

    public UpdateTrigger(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("timestamp", timestamp)
                .toString();
    }
}
