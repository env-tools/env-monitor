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
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.envtools.monitor.model.querylibrary.execution.QueryExecutionResult.ExecutionStatusE.*;

/**
 * Created: 13.03.16 1:34
 *
 * @author Yury Yakovlev
 */
public class JdbcQueryExecutionTask extends AbstractQueryExecutionTask{
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
    protected QueryExecutionResult doCall() {

        DataProviderType  queryType;
        QueryExecutionRequest queryExecutionRequest=null;
        QueryExecutionResult  queryExecutionResult=null;
        final ExecutionStatusE[] status = new ExecutionStatusE[1];

       /* try (Connection connection = jdbcDataSource.getConnection()){
            //TODO implement query execution
            //Line below is for testing only
            ResultSet rs = connection
                    .createStatement()
                    .executeQuery("SELECT * FROM LIB_QUERY");
            boolean hasNext = rs.next();

            //TODO prepare result according to request

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        */
        NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(jdbcDataSource);
        /*Получим n строк с запросом queryExecutionRequest.getQuery()
        * с параметрами queryExecutionRequest.getQueryParameters(),
        * из базы c параметрами jdbcDataSource*/
        jdbcTemplate.query(queryExecutionRequest.getQuery(),queryExecutionRequest.getQueryParameters(),
                new ResultSetExtractor<List<Map<String, Object>>>(){
                    public List<Map<String, Object>> extractData(ResultSet rs) throws SQLException, DataAccessException {
                        int rowNum = 0; //строки
                        try {
                            ResultSetMetaData md = rs.getMetaData();
                            int columns = md.getColumnCount();
                            List<Map<String, Object>> rows = new ArrayList<Map<String, Object>>();
                            while (rs.next()&& queryExecutionRequest.getRowCount()>rowNum){
                                Map<String, Object> row = new HashMap<String, Object>(columns);

                                    row.put(md.getColumnName(rowNum), rs.getObject(rowNum));
                                rowNum++;
                                rows.add(row);
                            }
                            LOGGER.info("Found queries: " + ExecutionStatusE.COMPLETED);
                            status[0] =ExecutionStatusE.COMPLETED;
                            return rows;
                        }catch (SQLException wx){
                            LOGGER.info("Found queries: " + ExecutionStatusE.ERROR);
                            status[0] =ExecutionStatusE.ERROR;
                        }

                        return null;
                    }
  //надо получить время, статус, количество полученных строк,List<Map<String, Object>>resultRows,
                    // errorMessage, Optional<Throwable> error
        });
        //Currently mock
        return queryExecutionResult;
    }



}
