package org.envtools.monitor.module.querylibrary.services.impl;

import org.apache.log4j.Logger;
import org.envtools.monitor.model.querylibrary.execution.QueryExecutionException;
import org.envtools.monitor.model.querylibrary.execution.QueryExecutionListener;
import org.envtools.monitor.model.querylibrary.execution.QueryExecutionRequest;
import org.envtools.monitor.model.querylibrary.execution.QueryExecutionResult;
import org.envtools.monitor.module.querylibrary.services.QueryExecutionService;

import java.util.UUID;

/**
 * Created: 27.02.16 3:42
 *
 * @author Yury Yakovlev
 */
public class QueryExecutionServiceImpl implements QueryExecutionService{

    private static final Logger LOGGER = Logger.getLogger(QueryExecutionServiceImpl.class);

    @Override
    public QueryExecutionResult execute(QueryExecutionRequest queryExecutionRequest) throws QueryExecutionException{
        switch (queryExecutionRequest.getQueryType()) {
            case JDBC:
                LOGGER.info("QueryExecutionServiceImpl.execute - executing request " + queryExecutionRequest);
                return executeJdbc(queryExecutionRequest);
            default:
                throw new UnsupportedOperationException(
                        String.format("Query type %s not supported", queryExecutionRequest.getQueryType()));
        }
    }

    @Override
    public UUID submitForExecution(QueryExecutionRequest queryExecutionRequest, QueryExecutionListener listener) {
        throw new UnsupportedOperationException("submitForExecution not implemented");
    }

    private QueryExecutionResult executeJdbc(QueryExecutionRequest queryExecutionRequest) throws QueryExecutionException {
        //TODO implement JDBC query execution
       return new QueryExecutionResult();
    }
}
