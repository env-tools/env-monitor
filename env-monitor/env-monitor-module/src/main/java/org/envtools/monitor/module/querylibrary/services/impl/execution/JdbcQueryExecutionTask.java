package org.envtools.monitor.module.querylibrary.services.impl.execution;


import com.google.common.collect.Lists;
import org.apache.commons.dbcp.BasicDataSource;
import org.apache.log4j.Logger;
import org.envtools.monitor.common.util.ExceptionReportingUtil;
import org.envtools.monitor.model.querylibrary.db.LibQuery;
import org.envtools.monitor.model.querylibrary.db.QueryExecution;
import org.envtools.monitor.model.querylibrary.db.QueryExecutionParam;
import org.envtools.monitor.model.querylibrary.execution.QueryExecutionRequest;
import org.envtools.monitor.model.querylibrary.execution.QueryExecutionResult;
import org.envtools.monitor.module.querylibrary.dao.LibQueryDao;
import org.envtools.monitor.module.querylibrary.dao.QueryExecutionDao;
import org.envtools.monitor.module.querylibrary.dao.QueryExecutionParamDao;
import org.envtools.monitor.module.querylibrary.services.DataSourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.envtools.monitor.model.querylibrary.execution.QueryExecutionResult.ExecutionStatusE;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;

import static org.envtools.monitor.model.querylibrary.execution.QueryExecutionResult.ExecutionStatusE.*;

/**
 * Created: 13.03.16 1:34
 *
 * @author Yury Yakovlev
 */
public class JdbcQueryExecutionTask extends AbstractQueryExecutionTask {

    private static final Logger LOGGER = Logger.getLogger(JdbcQueryExecutionTask.class);

    private BasicDataSource jdbcDataSource;

    public JdbcQueryExecutionTask(
            QueryExecutionRequest queryExecutionRequest,
            DataSourceService<BasicDataSource> jdbcDataSourceService) {
        super(queryExecutionRequest);

        jdbcDataSource = jdbcDataSourceService
                .getDataSourceForParams(
                        queryExecutionRequest.getDataSourceProperties());
    }

    private static class ResultDTO {
        public ExecutionStatusE status;
        public String errorMessage;
        public Throwable error;
    }


    @Override
    @Transactional
    protected QueryExecutionResult doCall() {

        final ResultDTO resultDTO = new ResultDTO();

        JdbcTemplate template = new JdbcTemplate(jdbcDataSource);
        template.setQueryTimeout(queryExecutionRequest.getTimeOutMs().intValue());
        NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(template);
        /*
        Получим n строк с запросом queryExecutionRequest.getQuery()
        * с параметрами queryExecutionRequest.getQueryParameters(),
        * из базы c параметрами jdbcDataSource
        * */
        List<Map<String, Object>> result = jdbcTemplate.query(queryExecutionRequest.getQuery(), queryExecutionRequest.getQueryParameters(),
                new ResultSetExtractor<List<Map<String, Object>>>() {
                    public List<Map<String, Object>> extractData(ResultSet rs) throws SQLException, DataAccessException {
                        int rowNum = 0; //строки
                        try {

                            ResultSetMetaData md = rs.getMetaData();
                            int columnCount = md.getColumnCount();
                            List<Map<String, Object>> rows = new ArrayList<>();
                            while (rs.next() && queryExecutionRequest.getRowCount() > rowNum) {
                                Map<String, Object> row = new LinkedHashMap<String, Object>(columnCount);

                                for (int iColumn = 1; iColumn <= columnCount; iColumn++) {
                                    row.put(md.getColumnName(iColumn), rs.getObject(iColumn));
                                }
                                rowNum++;
                                rows.add(row);
                            }

                            LOGGER.info(String.format("Found %d rows for query %s ", rowNum, queryExecutionRequest.getQuery()));

                            resultDTO.status = COMPLETED;

                            return rows;

                        } catch (Throwable t) {
                            LOGGER.info("Error executing query " + queryExecutionRequest.getQuery());
                            resultDTO.status = ERROR;
                            resultDTO.errorMessage = ExceptionReportingUtil.getExceptionMessage(t);
                            resultDTO.error = t;

                            return Lists.newArrayList();
                        }

                    }
                    //надо получить таймаут, статус, количество полученных строк,List<Map<String, Object>>resultRows,
                    // errorMessage, Optional<Throwable> error
                });


        return QueryExecutionResult
                .builder()
                .status(resultDTO.status)
                .elapsedTimeMs(200) //TODO count
                .returnedRowCount(result.size())
                .resultRows(result)
                .errorMessage(resultDTO.errorMessage)
                .error(resultDTO.error)
                .build();

    }

}
