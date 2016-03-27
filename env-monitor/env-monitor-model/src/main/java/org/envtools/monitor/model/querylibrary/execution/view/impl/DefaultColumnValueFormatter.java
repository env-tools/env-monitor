package org.envtools.monitor.model.querylibrary.execution.view.impl;

import org.apache.log4j.Logger;
import org.envtools.monitor.model.querylibrary.execution.view.ColumnValueFormatter;
import org.springframework.stereotype.Component;

/**
 * Created: 3/25/16 1:31 AM
 *
 * @author Yury Yakovlev
 */
@Component
public class DefaultColumnValueFormatter implements ColumnValueFormatter{

    @Override
    public String formatColumnValue(Object value) {
        //TODO implement nicely
        return value != null ? value.toString() : null;
    }

}
