package org.envtools.monitor.model.querylibrary.execution.view.impl;

import org.envtools.monitor.model.querylibrary.execution.view.ColumnValueFormatter;

/**
 * Created: 3/25/16 1:31 AM
 *
 * @author Yury Yakovlev
 */
public class DefaultColumnValueFormatter implements ColumnValueFormatter{

    @Override
    public String formatColumnValue(Object value) {
        //TODO implement nicely
        return value.toString();
    }

}
