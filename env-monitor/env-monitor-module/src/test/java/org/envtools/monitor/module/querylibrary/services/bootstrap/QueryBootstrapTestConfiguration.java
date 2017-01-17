package org.envtools.monitor.module.querylibrary.services.bootstrap;

/**
 * Created by IAvdeev on 12.01.2017.
 */

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.test.context.ContextConfiguration;

/**
 * Configuration for defining ZipArchiveBootstrapService as bean in bootstrap-test-context.xml
 */
@ImportResource("classpath:querylibrary/bootstrap/bootstrap-test-context.xml")
@Configuration
@ContextConfiguration()
public class QueryBootstrapTestConfiguration {
}
