package org.envtools.monitor.module.querylibrary.services.bootstrap;

import org.apache.log4j.Logger;
import org.apache.tomcat.jdbc.pool.DataSource;
import org.envtools.monitor.module.querylibrary.services.BootstrapService;
import org.h2.tools.RunScript;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;

/**
 * Load a tree of queries from plain sql file on application startup.
 */
public class SqlFileBootstrapService implements BootstrapService
{
    private static final Logger LOGGER = Logger.getLogger(SqlFileBootstrapService.class);

    @Autowired
    DataSource dataSource;

    /**
     * Location of sql file
     */
    @Value("${querylibrary.sql_bootstrapper.location}")
    Resource sqlFileResource;

    @Override
    public void bootstrap() throws Exception {
        Connection connection = this.dataSource.getConnection();
        InputStream stream = sqlFileResource.getInputStream();
        if (stream == null) {
            LOGGER.error(String.format("Couln't find sql boostrap file '%s'", sqlFileResource.toString()));
        } else {
            RunScript.execute(connection, new InputStreamReader(stream));
        }
    }
}
