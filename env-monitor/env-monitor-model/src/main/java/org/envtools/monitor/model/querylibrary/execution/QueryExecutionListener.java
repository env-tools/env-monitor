package org.envtools.monitor.model.querylibrary.execution;

import java.util.UUID;

/**
 * Created: 27.02.16 3:35
 *
 * @author Yury Yakovlev
 */
public interface QueryExecutionListener {

    default void onQueryCompleted(QueryExecutionResult queryResult) {}

    default void onQueryError(Throwable t) {}

    default void onQueryCancelled() {}

    default void onQueryTimeout() {}
}
