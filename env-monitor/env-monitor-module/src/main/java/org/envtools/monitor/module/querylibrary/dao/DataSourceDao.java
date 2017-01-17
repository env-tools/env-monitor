package org.envtools.monitor.module.querylibrary.dao;

import org.envtools.monitor.model.querylibrary.db.DataSource;

import java.util.List;

/**
 * Created by anastasiya on 07.03.16.
 */
public interface DataSourceDao extends Dao<DataSource, Long> {

    List<DataSource> getNameByText(String text);

    /**
     * Get DataSource by exact name
     *
     * @param name
     * @return
     */
    DataSource getByName(String name);

}
