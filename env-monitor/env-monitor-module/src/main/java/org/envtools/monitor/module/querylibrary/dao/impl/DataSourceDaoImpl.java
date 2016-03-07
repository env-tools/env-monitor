package org.envtools.monitor.module.querylibrary.dao.impl;

import edu.umd.cs.findbugs.annotations.Nullable;
import org.apache.log4j.Logger;
import org.envtools.monitor.model.querylibrary.db.DataSource;
import org.envtools.monitor.model.querylibrary.db.LibQuery;
import org.envtools.monitor.module.querylibrary.dao.DataSourceDao;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by anastasiya on 07.03.16.
 */
@Repository
public class DataSourceDaoImpl extends AbstractDbDao<DataSource, Long> implements DataSourceDao {


    private static final Logger LOGGER = Logger.getLogger(DataSourceDaoImpl.class);

    public DataSourceDaoImpl() {

        setClazz(DataSource.class);

        LOGGER.info("DataSourceDaoImpl created.");
    }

    @Override
    public List<DataSource> getNameByText(String text) {
        return em.createQuery("FROM DataSource WHERE name LIKE :textParam")
                .setParameter("textParam", createPatternString(text))
                .getResultList();
    }

    private String createPatternString(String text) {
        return "%" + text + "%";
    }
}
