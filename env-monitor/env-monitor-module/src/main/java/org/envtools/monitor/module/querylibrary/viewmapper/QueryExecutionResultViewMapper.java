package org.envtools.monitor.module.querylibrary.viewmapper;

import org.envtools.monitor.model.querylibrary.execution.QueryExecutionResult;
import org.envtools.monitor.model.querylibrary.execution.view.QueryExecutionResultView;

/**
 * Created: 3/25/16 1:00 AM
 *
 * @author Yury Yakovlev
 */
public interface QueryExecutionResultViewMapper {

    QueryExecutionResultView map(QueryExecutionResult queryExecutionResult);

    QueryExecutionResultView errorResult(Throwable t);

    QueryExecutionResultView cancelledResult();

    QueryExecutionResultView timeoutResult();
}
