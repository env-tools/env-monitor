package org.envtools.monitor.model.querylibrary.execution.view;

import org.envtools.monitor.model.querylibrary.execution.QueryExecutionResult;

/**
 * Created: 3/25/16 1:00 AM
 *
 * @author Yury Yakovlev
 */
public interface QueryExecutionResultViewMapper {

    QueryExecutionResultView map(QueryExecutionResult queryExecutionResult);

    QueryExecutionResultView errorResult(Throwable t);

}
