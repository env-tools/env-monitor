package org.envtools.monitor.module.querylibrary.viewmapper.impl;

import org.envtools.monitor.module.querylibrary.viewmapper.ColumnValueFormatter;
import org.springframework.stereotype.Component;

/**
 * Created: 3/25/16 1:31 AM
 *
 * @author Yury Yakovlev
 */
@Component
public class DefaultColumnValueFormatter implements ColumnValueFormatter {

    @Override
    public String formatColumnValue(Object value) {
        //TODO implement nicely
        return value != null ? value.toString() : null;
    }

}
