package org.envtools.monitor.module.querylibrary.services.impl.execution;


import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.lang.mutable.MutableInt;
import org.apache.commons.lang.time.StopWatch;
import org.apache.log4j.Logger;
import org.envtools.monitor.model.querylibrary.QueryParamType;
import org.envtools.monitor.model.querylibrary.execution.QueryExecutionNextResultRequest;
import org.envtools.monitor.model.querylibrary.execution.QueryExecutionRequest;
import org.envtools.monitor.model.querylibrary.execution.QueryExecutionResult;
import org.envtools.monitor.module.querylibrary.services.DataSourceService;
import org.envtools.monitor.module.querylibrary.services.QueryExecutionTaskRegistry;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.sql.*;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static org.envtools.monitor.model.querylibrary.execution.QueryExecutionResult.ExecutionStatusE.*;

/**
 * Created: 13.03.16 1:34
 *
 * @author Yury Yakovlev
 */
public class JdbcQueryExecutionTask extends AbstractQueryExecutionTask {

    private static final Logger LOGGER = Logger.getLogger(JdbcQueryExecutionTask.class);

    private static final int NEXT_REQUEST_TIMEOUT_SEC = 600;

    private static final List<Map<String, Object>> NOT_USED = Collections.emptyList();

    private BasicDataSource jdbcDataSource;

    private StopWatch timer = new StopWatch();

    public JdbcQueryExecutionTask(
            QueryExecutionRequest queryExecutionRequest,
            QueryExecutionTaskRegistry taskRegistry,
            DataSourceService<BasicDataSource> jdbcDataSourceService) {
        super(queryExecutionRequest, taskRegistry);

        jdbcDataSource = jdbcDataSourceService
                .getDataSourceForParams(
                        queryExecutionRequest.getDataSourceProperties());
    }

    @Override
    protected void doRun() {

        JdbcTemplate template = new JdbcTemplate(jdbcDataSource);
        template.setQueryTimeout(queryExecutionRequest.getTimeOutMs().intValue());
        NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(template);

        try {

            timer.start();

            jdbcTemplate.query(
                    queryExecutionRequest.getQuery(),
                    enrichWithTypes(
                            queryExecutionRequest.getQueryParameters(),
                            queryExecutionRequest.getQueryParameterTypes()),
                    (ResultSet rs) -> extractRows(rs));
        } catch (Throwable t) {
            LOGGER.info("JdbcQueryExecutionTask.doRun - " +
                    String.format("query execution failed for: %s with params %s after %s",
                            queryExecutionRequest.getQuery(),
                            queryExecutionRequest.getQueryParameters(),
                            timer)
                    , t);

            postResult(QueryExecutionResult.ofError(
                    getOperationId(),
                    t));
        }
    }

    private Map<String, Object> enrichWithTypes(Map<String, Object> parameters,
                                                Map<String, QueryParamType> parameterTypes) {

        if (parameters == null) {
            return Maps.newHashMap();
        }

        if (parameterTypes == null) {
            return parameters;
        }

        Map<String, Object> resultParameters = Maps.newLinkedHashMap();
        for (Map.Entry<String, Object> parameter : parameters.entrySet()) {
            QueryParamType paramType = parameterTypes.get(parameter.getKey());

            String strValue = parameter.getValue().toString();
            Object value = strValue;

            //TODO refactor logic below, add methods
            switch (paramType) {
                case DATE:
                case TIMESTAMP:
                    //TODO support these types
                    throw new IllegalArgumentException(
                           String.format("Parameters with type %s not supported yet", parameter.getKey()));
                case NUMBER:

                    try {
                       if (strValue.contains(".")) {
                           value = Double.parseDouble(strValue);
                       } else {
                           value = Integer.parseInt(strValue);
                       }
                    } catch (NumberFormatException nfe) {
                        throw new RuntimeException("Invalid numeric value " + strValue, nfe);
                    }
            }

            resultParameters.put(parameter.getKey(), value);
        }

        return resultParameters;
    }

    /**
     * Callback implementation for Spring template
     *
     * @param rs Resultset that is opened and closed by Spring automatically for us
     * @return Result is actually don't needed as we process results ourselves
     *         by posting them to a separate queue
     */
    private List<Map<String, Object>> extractRows(ResultSet rs) {
        {
            String operationId = queryExecutionRequest.getOperationId();
            MutableInt currentRowNum = new MutableInt(0);
            MutableInt maxRowCount = new MutableInt(queryExecutionRequest.getRowCount()); //строки

            try {

                ResultSetMetaData md = rs.getMetaData();
                List<String> columns = getColumns(md);

                //Check for cancellation
                if (isCancelled()) {
                    postResult(QueryExecutionResult.ofCancel(operationId));
                    return NOT_USED;
                }

                List<Map<String, Object>> currentResultRows = Lists.newArrayList();

                //Loop while we don't enter a final state
                //when we don't need this ResultSet any more
                while (getNextRow(currentRowNum, maxRowCount,
                        rs, currentResultRows, columns)) {

                    //If we get here, it means that rs.next() has been called
                    //and we are ready to read row data

                    //Check for cancellation from other thread
                    if (isCancelled()) {
                        postResult(QueryExecutionResult.ofCancel(operationId));
                        return NOT_USED;
                    }

                    //Add row to the result
                    addRowData(md, currentResultRows, rs);

                    //Update row number for the row we have just processed
                    currentRowNum.increment();

                }

                if (isCancelled) {
                    postResult(QueryExecutionResult.ofCancel(operationId));
                }

            } catch (Throwable t) {

                LOGGER.info("JdbcQueryExecutionTask.extract - " +
                        String.format("query result extraction failed for: %s with params %s",
                                queryExecutionRequest.getQuery(),
                                queryExecutionRequest.getQueryParameters())
                        , t);

                postResult(QueryExecutionResult.ofError(
                        getOperationId(), t));
            }

            return NOT_USED;
        }
    }

    private List<String> getColumns(ResultSetMetaData md) throws SQLException {
        int columnCount = md.getColumnCount();
        List<String> columns = Lists.newArrayList();

        for (int iColumn = 1; iColumn <= columnCount; iColumn++) {
            columns.add(md.getColumnName(iColumn));
        }
        return columns;
    }

    private void addRowData(
            ResultSetMetaData md,
            List<Map<String, Object>> rows,
            ResultSet rs) throws SQLException {
        int columnCount = md.getColumnCount();
        Map<String, Object> row = new LinkedHashMap<>(columnCount);

        for (int iColumn = 1; iColumn <= columnCount; iColumn++) {
            row.put(md.getColumnName(iColumn), rs.getObject(iColumn));
        }

        rows.add(row);
    }

    private boolean getNextRow(MutableInt rowNum,
                               MutableInt maxRowCount,
                               ResultSet rs,
                               List<Map<String, Object>> resultRows,
                               List<String> columns) throws SQLException {

        boolean hasMoreRows = rs.next();

        if (rowNum.intValue() > maxRowCount.intValue()) {
            //This should never happen
            throw new IllegalArgumentException(
                    String.format("rowNum %s exceeded maxRowCount %s",
                            rowNum, maxRowCount));
        }

        if (rowNum.intValue() < maxRowCount.intValue()) {
            if (!hasMoreRows) {
                //No more rows
                putFinalResult(resultRows, columns);
                return false;
            } else {
                //We have more rows to read  within current limitation (maxRows)
                return true;
            }
        }

        if (rowNum.intValue() == maxRowCount.intValue()) {

            //Take even 1 more row to see if we should continue
            if (!hasMoreRows) {
                //No more rows
                //Result set is complete
                putFinalResult(resultRows, columns);
                return false;
            } else {
                //Post rows that we already have
                putIncompleteResult(resultRows, columns);

                //We have read 1 more row for the next request
                //Wait for next rows to be requested
                try {

                    //This statement might be interrupted on Cancel
                    QueryExecutionNextResultRequest nextResultRequest =
                            nextResultRequests.poll(NEXT_REQUEST_TIMEOUT_SEC, TimeUnit.SECONDS);

                    if (nextResultRequest == null) {
                        //Timeout elapsed
                        //We will terminate this task
                        //No results are posted
                        return false;
                    }

                    //Ready to process next portion of result as requested
                    maxRowCount.setValue(nextResultRequest.getRowCount());
                    resultRows.clear();
                    rowNum.setValue(0);

                    //Restarting the timer
                    timer.start();

                    //We have to continue processing
                    return true;

                } catch (InterruptedException ie) {
                    LOGGER.info("JdbcQueryExecutionTask.getNextRow - interrupted while waiting for next result request");
                    isCancelled = true;
                    return false;
                }
            }
        }
        return false;
    }

    private void putFinalResult(List<Map<String, Object>> rows,
                                List<String> columns) {
        LOGGER.info("JdbcQueryExecutionTask.putFinalResult : " + rows);

        timer.stop();

        postResult(
                QueryExecutionResult
                        .builder()
                        .operationId(getOperationId())
                        .status(COMPLETED)
                        .elapsedTimeMs(timer.getTime())
                        .returnedRowCount(rows.size())
                        .resultRows(rows)
                        .resultColumns(columns)
                        .build()
        );

        //Actually, this timer will no longer be used
        timer.reset();
    }

    private void putIncompleteResult(List<Map<String, Object>> rows,
                                     List<String> columns) {
        LOGGER.info("JdbcQueryExecutionTask.putIncompleteResult : " + rows);

        //Timer will be restarted
        //when next portion of result will be requested
        timer.stop();

        postResult(
                QueryExecutionResult
                        .builder()
                        .operationId(getOperationId())
                        .status(HAS_MORE_DATA)
                        .elapsedTimeMs(timer.getTime())
                        .returnedRowCount(rows.size())
                        .resultRows(rows)
                        .resultColumns(columns)
                        .build()
        );

        //Timer might be reused later
        timer.reset();

    }
}
