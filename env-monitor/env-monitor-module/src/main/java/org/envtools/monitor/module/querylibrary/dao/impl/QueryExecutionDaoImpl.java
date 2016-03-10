package org.envtools.monitor.module.querylibrary.dao.impl;

import org.apache.log4j.Logger;
import org.envtools.monitor.model.querylibrary.db.LibQuery;
import org.envtools.monitor.model.querylibrary.db.QueryExecution;
import org.envtools.monitor.module.querylibrary.dao.QueryExecutionDao;
import org.springframework.stereotype.Repository;

import java.time.LocalTime;
import java.util.List;

/**
 * Created: 10.03.16 21:57
 *
 * @author Anastasiya Plotnikova
 */
@Repository
public class QueryExecutionDaoImpl extends AbstractDbDao<QueryExecution, Long> implements QueryExecutionDao {

    private static final Logger LOGGER = Logger.getLogger(QueryExecutionDaoImpl.class);

    public QueryExecutionDaoImpl() {

        setClazz(QueryExecution.class);

        LOGGER.info("QueryExecutionDaoImpl created.");
    }

    @Override
    public List<QueryExecution> getUserByText(String text) {
        return em.createQuery("FROM QueryExecution WHERE user LIKE :textFragmentParam")
                .setParameter("textFragmentParam", createPatternString(text))
                .getResultList();
    }

    @Override
    public List<QueryExecution> getstartTimestamp(LocalTime time) {
        return em.createQuery("FROM QueryExecution WHERE startTimestamp LIKE :textFragmentParam")
                .setParameter("textFragmentParam",createPatternTime(time)).getResultList();
                //.setParameter("textFragmentParam", createPatternTime(time))
                //.getResultList();
    }

    @Override
    public List<QueryExecution> getEndTimestamp(LocalTime time) {
        return em.createQuery("FROM QueryExecution WHERE endTimestamp LIKE :textFragmentParam")
                .setParameter("textFragmentParam",createPatternTime(time)).getResultList();
    }


    private String createPatternString(String text) {
        return "%" + text + "%";
    }

    private LocalTime createPatternTime(LocalTime time) {
        return  time;
    }
}
