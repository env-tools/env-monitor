package org.envtools.monitor.module.querylibrary.dao.impl;

import org.apache.log4j.Logger;
import org.envtools.monitor.model.querylibrary.db.QueryExecution;
import org.envtools.monitor.model.querylibrary.db.QueryExecutionParam;
import org.envtools.monitor.module.querylibrary.dao.QueryExecutionParamDao;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created: 10.03.16 22:40
 *
 * @author Anastasiya Plotnikova
 */
@Repository
public class QueryExecutionParamDaoImpl extends AbstractDbDao<QueryExecutionParam, Long> implements QueryExecutionParamDao {

    private static final Logger LOGGER = Logger.getLogger(QueryExecutionParamDaoImpl.class);

    public QueryExecutionParamDaoImpl() {

        setClazz(QueryExecutionParam.class);

        LOGGER.info("QueryExecutionParamDaoImpl created.");
    }

    @Override
    public List<QueryExecutionParam> getNameByText(String text) {
        return em.createQuery("FROM QueryExecutionParam WHERE name LIKE :textFragmentParam")
                .setParameter("textFragmentParam", createPatternString(text))
                .getResultList();
    }

    @Override
    public List<QueryExecutionParam> getValueByText(String text) {
        return em.createQuery("FROM QueryExecutionParam WHERE value LIKE :textFragmentParam")
                .setParameter("textFragmentParam", createPatternString(text))
                .getResultList();
    }

    private String createPatternString(String text) {
        return "%" + text + "%";
    }
}
