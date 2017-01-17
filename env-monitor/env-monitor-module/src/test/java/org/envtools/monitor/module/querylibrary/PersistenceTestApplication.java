package org.envtools.monitor.module.querylibrary;

import org.envtools.monitor.model.querylibrary.db.*;
import org.envtools.monitor.model.querylibrary.execution.QueryExecutionException;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Created: 04.03.16 22:08
 *
 * @author Yury Yakovlev
 */
@SpringBootApplication
@ComponentScan(basePackages = "org.envtools.*")
@EnableJpaRepositories("org.envtools.monitor.module.querylibrary.repo.*")
@EntityScan(basePackageClasses = {LibQuery.class, Category.class, DataSource.class, DataSourceProperty.class,
        QueryExecution.class, QueryExecutionParam.class, QueryParam.class })
@EnableAutoConfiguration
public class PersistenceTestApplication {
}
