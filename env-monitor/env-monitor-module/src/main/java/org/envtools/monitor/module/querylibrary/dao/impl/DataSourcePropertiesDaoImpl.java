package org.envtools.monitor.module.querylibrary.dao.impl;

import org.envtools.monitor.model.querylibrary.db.DataSourceProperties;
import org.envtools.monitor.module.querylibrary.dao.DataSourcePropertiesDao;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created: 07.03.16 21:27
 *
 * @author Anastasiya Plotnikova
 */
@Repository
public class DataSourcePropertiesDaoImpl extends AbstractDbDao<DataSourceProperties, Long> implements DataSourcePropertiesDao{

    @Override
    public List<DataSourceProperties> getValueByText(String text) {
        return  em.createQuery("FROM DataSourceProperties WHERE value LIKE :textParam")
                .setParameter("textParam", createPatternString(text))
                .getResultList();
    }

    private String createPatternString(String text) {
        return "%" + text + "%";
    }
}
