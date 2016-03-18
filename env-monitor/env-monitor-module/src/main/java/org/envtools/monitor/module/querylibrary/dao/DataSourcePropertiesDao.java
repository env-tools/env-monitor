package org.envtools.monitor.module.querylibrary.dao;

import org.envtools.monitor.model.querylibrary.db.DataSourceProperty;

import java.util.List;

/**
 * Created: 07.03.16 21:23
 *
 * @author Anastasiya Plotnikova
 */
public interface DataSourcePropertiesDao extends Dao<DataSourceProperty, Long > {

    List<DataSourceProperty> getValueByText(String text);

}
