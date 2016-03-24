package org.envtools.monitor.module.querylibrary.services.impl.execution;


import org.apache.commons.dbcp.BasicDataSource;
import org.apache.log4j.Logger;
import org.envtools.monitor.model.querylibrary.DataProviderType;
import org.envtools.monitor.model.querylibrary.db.Category;
import org.envtools.monitor.model.querylibrary.execution.QueryExecutionNextResultRequest;
import org.envtools.monitor.model.querylibrary.execution.QueryExecutionRequest;
import org.envtools.monitor.model.querylibrary.execution.QueryExecutionResult;
import org.envtools.monitor.module.querylibrary.services.DataSourceService;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.RowMapperResultSetExtractor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.envtools.monitor.model.querylibrary.execution.QueryExecutionResult.ExecutionStatusE;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.transaction.annotation.Transactional;
import javax.sql.DataSource;
import java.sql.*;
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
    private static final String selectSql = "SELECT * FROM Category";


    public JdbcQueryExecutionTask(
            QueryExecutionRequest queryExecutionRequest,
            DataSourceService<BasicDataSource> jdbcDataSourceService) {
        super(queryExecutionRequest);

        jdbcDataSource = jdbcDataSourceService
                .getDataSourceForParams(
                        queryExecutionRequest.getDataSourceProperties());
    }


    @Override
    // @Transactional(timeout=queryExecutionRequest.)
    protected QueryExecutionResult doCall() {
        final ExecutionStatusE[] status = new ExecutionStatusE[1];
        final Optional<String>[] errorMessage = null;
        final Optional<Throwable>[] error = null;
        //TODO implement query execution
        //TODO prepare result according to request
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
                            int columns = md.getColumnCount();
                            List<Map<String, Object>> rows = new ArrayList<Map<String, Object>>();
                            while (rs.next() && queryExecutionRequest.getRowCount() > rowNum) {
                                Map<String, Object> row = new HashMap<String, Object>(columns);

                                row.put(md.getColumnName(rowNum), rs.getObject(rowNum));
                                rowNum++;
                                rows.add(row);
                            }
                            LOGGER.info("Found queries: " + ExecutionStatusE.COMPLETED);
                            status[0] = ExecutionStatusE.COMPLETED;
                            return rows;
                        } catch (SQLException wx) {
                            LOGGER.info("Found queries: " + ExecutionStatusE.ERROR + "ErrorMassage: " + wx);
                            status[0] = ExecutionStatusE.ERROR;
                            errorMessage[0] = Optional.of(wx.toString());
                        } catch (Throwable t) {
                            error[0] = Optional.of(t);
                        }

                        return null;
                    }
                    //надо получить таймаут, статус, количество полученных строк,List<Map<String, Object>>resultRows,
                    // errorMessage, Optional<Throwable> error
                });

        //Currently mock
        return new QueryExecutionResult(status[0], template.getQueryTimeout(), result.size(), result, errorMessage[0], error[0]);

    }

}
