package org.envtools.monitor.module.querylibrary.dao.impl;

import org.apache.log4j.Logger;
import org.envtools.monitor.model.querylibrary.db.Category;
import org.envtools.monitor.model.querylibrary.db.LibQuery;
import org.envtools.monitor.model.querylibrary.db.QueryParam;
import org.envtools.monitor.module.querylibrary.dao.QueryParamDao;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created: 10.03.16 21:29
 *
 * @author Anastasiya Plotnikova
 */
@Repository
public class QueryParamDaoImpl extends AbstractDbDao<QueryParam, Long> implements QueryParamDao {

    private static final Logger LOGGER = Logger.getLogger(CategoryDaoImpl.class);

    public QueryParamDaoImpl() {

        setClazz(QueryParam.class);

        LOGGER.info("QueryParamDaoImpl created.");
    }

    @Override
    public List<QueryParam> getNameQueryParamByText(String text) {
        return em.createQuery("FROM QueryParam WHERE name LIKE :textParam")
                .setParameter("textParam", createPatternString(text))
                .getResultList();
    }

    private String createPatternString(String text) {
        return "%" + text + "%";
    }
}
