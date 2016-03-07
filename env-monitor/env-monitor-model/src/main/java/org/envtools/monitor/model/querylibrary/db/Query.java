package org.envtools.monitor.model.querylibrary.db;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import javax.persistence.*;

/**
 * Created: 2/23/16 12:30 AM
 *
 * @author Yury Yakovlev
 */
@Entity
public class Query extends AbstractDbIdentifiable {

    public Query() {
    }

    private String text;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }


    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).
                append("text", text).
                toString();
    }
}
