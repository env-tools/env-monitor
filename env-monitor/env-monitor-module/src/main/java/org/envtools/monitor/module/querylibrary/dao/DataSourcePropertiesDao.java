package org.envtools.monitor.module.querylibrary.dao;

import org.envtools.monitor.model.querylibrary.db.DataSourceProperties;
import org.envtools.monitor.module.querylibrary.dao.impl.AbstractDbDao;

import java.util.List;

/**
 * Created: 07.03.16 21:23
 *
 * @author Anastasiya Plotnikova
 */
public interface DataSourcePropertiesDao extends Dao<DataSourceProperties, Long > {
    List<DataSourceProperties> getValueByText(String text);
}
