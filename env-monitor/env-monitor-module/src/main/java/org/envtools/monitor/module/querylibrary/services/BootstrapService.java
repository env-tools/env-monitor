package org.envtools.monitor.module.querylibrary.services;

import org.apache.tomcat.jdbc.pool.DataSource;

/**
 * Created by IAvdeev on 11.01.2017.
 */
public interface BootstrapService {
    /**
     * Bootstraps categories and queries from external source (e.g. file) into db.
     *
     * @throws Exception when external file not found or it have invalid format.
     */
    void bootstrap() throws Exception;
}
