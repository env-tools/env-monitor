package org.envtools.monitor.module.querylibrary.services;

import org.envtools.monitor.model.querylibrary.execution.*;

/**
 * Created: 23.02.16 3:06
 *
 * @author Yury Yakovlev
 *
 * Supports synchronous and asynchronous execution of queries
 * Queries may return partial results, so that later execution may continue
 *
 */
public interface QueryExecutionService {

    /**
     * Synchronous operation to execute query and get first result
     *
     * @param queryExecutionRequest Quite new request to be executed
     * @return First result (complete or partial) of the query
     * @throws QueryExecutionException
     */
    QueryExecutionResult execute(QueryExecutionRequest queryExecutionRequest) throws QueryExecutionException;

    /**
     * Synchronous operation to continue execution of a query already posted
     * @param queryExecutionNextResultRequest Reference to a request already posted
     * @return Next result (complete or partial) of the query
     * @throws QueryExecutionException
     */
    //QueryExecutionResult executeNext(QueryExecutionNextResultRequest queryExecutionNextResultRequest)  throws QueryExecutionException;

    /**
     * Asynchronous operation to execute query with appropriate result listener
     * @param queryExecutionRequest Quite new request to be executed
     * @param listener Observer to react to query final state
     * @throws QueryExecutionException
     */
    void submitForExecution(QueryExecutionRequest queryExecutionRequest,
                            QueryExecutionListener listener) throws QueryExecutionException;

    void submitForNextResult(QueryExecutionNextResultRequest queryExecutionNextResultRequest,
                             QueryExecutionListener listener) throws QueryExecutionException;

    /**
     * Request query cancel
     * @param cancelRequest Reference to a query already posted
     */
    void cancel(QueryExecutionCancelRequest cancelRequest);

}
