package org.envtools.monitor.module.querylibrary;

import org.envtools.monitor.model.querylibrary.db.DataSource;
import org.envtools.monitor.model.querylibrary.db.DataSourceProperties;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Created: 07.03.16 21:35
 *
 * @author Anastasiya Plotnikova
 */
@SpringBootApplication
@ComponentScan(basePackages = "org.envtools.*")
@EnableJpaRepositories("org.envtools.monitor.module.querylibrary.repo.*")
@EntityScan(basePackageClasses = DataSourceProperties.class)
@EnableAutoConfiguration
public class PersistenceTestDataSourceProperties {
}
