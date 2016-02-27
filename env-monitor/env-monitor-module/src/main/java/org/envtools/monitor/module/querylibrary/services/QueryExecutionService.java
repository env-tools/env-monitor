package org.envtools.monitor.module.querylibrary.services;

import org.envtools.monitor.model.querylibrary.execution.QueryExecutionException;
import org.envtools.monitor.model.querylibrary.execution.QueryExecutionListener;
import org.envtools.monitor.model.querylibrary.execution.QueryExecutionRequest;
import org.envtools.monitor.model.querylibrary.execution.QueryExecutionResult;

import java.util.UUID;

/**
 * Created: 23.02.16 3:06
 *
 * @author Yury Yakovlev
 */
public interface QueryExecutionService {

    QueryExecutionResult execute(QueryExecutionRequest queryExecutionRequest) throws QueryExecutionException;

    UUID submitForExecution(QueryExecutionRequest queryExecutionRequest,
                            QueryExecutionListener listener) throws QueryExecutionException;

}
