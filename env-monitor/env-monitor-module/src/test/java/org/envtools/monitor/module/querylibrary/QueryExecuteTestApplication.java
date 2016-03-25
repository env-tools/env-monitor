package org.envtools.monitor.module.querylibrary;

import org.envtools.monitor.module.querylibrary.services.impl.datasource.JdbcDataSourceService;
import org.envtools.monitor.module.querylibrary.services.impl.execution.QueryExecutionServiceImpl;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.context.annotation.ComponentScan;

/**
 * Created by sergey on 23.03.2016.
 */

@SpringBootApplication
@ComponentScan(basePackages = "org.envtools.*")
@EnableAutoConfiguration
public class QueryExecuteTestApplication {
}
