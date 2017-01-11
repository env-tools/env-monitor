package org.envtools.monitor.module.querylibrary.services.bootstrap;

import org.apache.log4j.Logger;
import org.apache.tomcat.jdbc.pool.DataSource;
import org.envtools.monitor.module.querylibrary.services.TreeBootstrapService;
import org.h2.tools.RunScript;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;

/**
 * Loads a tree of queries from plain sql file on application startup.
 */
public class SqlFileBootstrapService implements TreeBootstrapService
{
    private static final Logger LOGGER = Logger.getLogger(SqlFileBootstrapService.class);

    @Autowired
    DataSource dataSource;

    /**
     * Location of sql file relative to application classpath.
     */
    @Value("${querylibrary.sql_bootstrapper.location}")
    String fileLocation;

    @Override
    public void bootstrap(DataSource dataSource) throws Exception {
        Connection connection = this.dataSource.getConnection();
        InputStream stream = this.getClass().getClassLoader().getResourceAsStream(fileLocation);
        if (stream == null) {
            LOGGER.error(String.format("Couln't find sql boostrap file '%s'", fileLocation));
        } else {
            RunScript.execute(connection, new InputStreamReader(stream));
        }
    }
}
