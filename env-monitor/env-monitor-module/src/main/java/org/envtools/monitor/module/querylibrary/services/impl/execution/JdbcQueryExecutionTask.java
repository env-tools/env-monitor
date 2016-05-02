package org.envtools.monitor.module.querylibrary.services.impl.execution;


import com.google.common.collect.Lists;
import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.lang.mutable.MutableInt;
import org.apache.commons.lang.time.StopWatch;
import org.apache.log4j.Logger;
import org.envtools.monitor.model.querylibrary.execution.QueryExecutionNextResultRequest;
import org.envtools.monitor.model.querylibrary.execution.QueryExecutionRequest;
import org.envtools.monitor.model.querylibrary.execution.QueryExecutionResult;
import org.envtools.monitor.module.querylibrary.services.DataSourceService;
import org.envtools.monitor.module.querylibrary.services.QueryExecutionTaskRegistry;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.envtools.monitor.model.querylibrary.execution.QueryExecutionResult.ExecutionStatusE;
import org.springframework.transaction.annotation.Transactional;

import java.sql.*;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

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
                    queryExecutionRequest.getQueryParameters(),
                    (ResultSet rs) -> extractRows(rs));
        } catch (Throwable t) {

            LOGGER.info("JdbcQueryExecutionTask.doRun - " +
                    String.format("query execution failed for: %s with params %s",
                            queryExecutionRequest.getQuery(),
                            queryExecutionRequest.getQueryParameters())
                            , t);

            postResult(QueryExecutionResult.ofError(
                    queryExecutionRequest.getOperationId(),
                    t));
        }
    }

    private List<Map<String, Object>> extractRows(ResultSet rs) {
        {
            String operationId = queryExecutionRequest.getOperationId();
            MutableInt currentRowNum = new MutableInt(0);
            MutableInt maxRowCount = new MutableInt(queryExecutionRequest.getRowCount()); //строки

            try {

                ResultSetMetaData md = rs.getMetaData();

                //Check for cancellation
                if (isCancelled()) {
                    postResult(QueryExecutionResult.ofCancel(operationId));
                    return NOT_USED;
                }

                List<Map<String, Object>> currentResultRows = Lists.newArrayList();

                while ( !allResultSetRowsProcessed(currentRowNum, maxRowCount,
                        rs, currentResultRows)) {

                    //Check for cancellation
                    if (isCancelled()) {
                        postResult(QueryExecutionResult.ofCancel(operationId));
                        return NOT_USED;
                    }

                    addRowData(md, currentResultRows, rs);
                    currentRowNum.increment();

                }

            } catch (Throwable t) {

                LOGGER.info("JdbcQueryExecutionTask.extract - " +
                        String.format("query result extraction failed for: %s with params %s",
                                queryExecutionRequest.getQuery(),
                                queryExecutionRequest.getQueryParameters())
                        , t);

                postResult(QueryExecutionResult.ofError(
                        queryExecutionRequest.getOperationId(), t));
            }

            return NOT_USED;
        }
    }

    private void addRowData(
            ResultSetMetaData md,
            List<Map<String, Object>> rows,
            ResultSet rs)  throws SQLException {
        int columnCount = md.getColumnCount();
        Map<String, Object> row = new LinkedHashMap<>(columnCount);

        for (int iColumn = 1; iColumn <= columnCount; iColumn++) {
            row.put(md.getColumnName(iColumn), rs.getObject(iColumn));
        }

        rows.add(row);
    }

    private boolean allResultSetRowsProcessed(MutableInt rowNum,
                                              MutableInt maxRowCount,
                                              ResultSet rs,
                                              List<Map<String, Object>> rows) throws SQLException {

        boolean hasMoreRows = rs.next();

        if (rowNum.intValue() > maxRowCount.intValue()) {
            throw new IllegalArgumentException(
                    String.format("rowNum %s exceeded maxRowCount %s",
                            rowNum, maxRowCount));
        }

        if (rowNum.intValue() < maxRowCount.intValue()) {
            if (!hasMoreRows) {
                //No more rows
                putFinalResult(rows);
                return true;
            } else {
                //We have more rows to read  within current limitation (maxRows)
                return false;
            }
        }

        if (rowNum.intValue() == maxRowCount.intValue()) {

            //Take even 1 more row to see if we should continue
            if (!rs.next()) {
                //No more rows
                //Result set is complete
                putFinalResult(rows);
                return true;
            } else {
                //Post rows that we already have
                putIncompleteResult(rows);

                //Wait for next rows to be requested
                try {
                    QueryExecutionNextResultRequest nextResultRequest =
                            nextResultRequests.poll(NEXT_REQUEST_TIMEOUT_SEC, TimeUnit.SECONDS);

                    if (nextResultRequest == null) {
                        //Timeout elapsed
                        //We will terminate this task
                        //No results are posted
                        return true;
                    }

                    //Ready to process next portion of result as requested
                    maxRowCount.setValue(nextResultRequest.getRowCount());
                    rows.clear();
                    rowNum.setValue(0);

                    timer.start();

                    //We have to continue processing
                    return false;

                } catch (InterruptedException ie) {
                    LOGGER.info("JdbcQueryExecutionTask.allResultSetRowsProcessed - interrupted while waiting for next result request",
                            ie);
                    isCancelled = true;
                    return true;
                }
            }
        }
        return true;
    }

    private void putFinalResult(List<Map<String, Object>> rows) {
        LOGGER.info("JdbcQueryExecutionTask.putFinalResult : " + rows);

        timer.stop();

        postResult(
                QueryExecutionResult
                        .builder()
                        .status(COMPLETED)
                        .elapsedTimeMs(timer.getTime())
                        .returnedRowCount(rows.size())
                        .resultRows(rows)
                        .build()
        );

        //Actually, this timer will no longer be used
        timer.reset();
    }

    private void putIncompleteResult(List<Map<String, Object>> rows) {
        LOGGER.info("JdbcQueryExecutionTask.putIncompleteResult : " + rows);

        timer.stop();

        postResult(
                QueryExecutionResult
                        .builder()
                        .status(HAS_MORE_DATA)
                        .elapsedTimeMs(timer.getTime())
                        .returnedRowCount(rows.size())
                        .resultRows(rows)
                        .build()
        );

        //Timer might be reused later
        timer.reset();

    }
}
