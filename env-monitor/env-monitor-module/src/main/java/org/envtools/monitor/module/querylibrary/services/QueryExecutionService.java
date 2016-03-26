package org.envtools.monitor.module.querylibrary.services;

import org.envtools.monitor.model.querylibrary.execution.*;

import java.util.UUID;

/**
 * Created: 23.02.16 3:06
 *
 * @author Yury Yakovlev
 */
public interface QueryExecutionService {

    QueryExecutionResult execute(QueryExecutionRequest queryExecutionRequest) throws QueryExecutionException;

    void submitForExecution(QueryExecutionRequest queryExecutionRequest,
                            QueryExecutionListener listener) throws QueryExecutionException;

    void cancel(QueryExecutionCancelRequest cancelRequest);

}
