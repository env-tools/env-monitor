package org.envtools.monitor.module.querylibrary;

import org.envtools.monitor.model.querylibrary.db.LibQuery;
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
@EntityScan(basePackageClasses = LibQuery.class)
@EnableAutoConfiguration
public class PersistenceTestApplication {
}
