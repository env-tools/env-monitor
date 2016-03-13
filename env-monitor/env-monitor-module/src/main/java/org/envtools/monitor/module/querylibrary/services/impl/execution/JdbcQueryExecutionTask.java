package org.envtools.monitor.module.querylibrary.services.impl.execution;

import org.apache.commons.dbcp.BasicDataSource;
import org.envtools.monitor.model.querylibrary.execution.QueryExecutionRequest;
import org.envtools.monitor.model.querylibrary.execution.QueryExecutionResult;
import org.envtools.monitor.module.querylibrary.services.DataSourceService;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created: 13.03.16 1:34
 *
 * @author Yury Yakovlev
 */
public class JdbcQueryExecutionTask extends AbstractQueryExecutionTask{

    private BasicDataSource jdbcDataSource;

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

        QueryExecutionResult queryExecutionResult = null;

        try (Connection connection = jdbcDataSource.getConnection()){
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

        //Currently mock
        return queryExecutionResult;
    }
}
