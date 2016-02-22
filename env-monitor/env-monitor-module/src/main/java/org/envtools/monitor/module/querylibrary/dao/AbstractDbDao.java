package org.envtools.monitor.module.querylibrary.dao;

import org.hibernate.*;

import java.io.Serializable;
import java.util.List;

/**
 * Created: 2/23/16 12:48 AM
 *
 * @author Yury Yakovlev
 */
public abstract class AbstractDbDao {

    private SessionFactory sessionFactory;

    public AbstractDbDao(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    protected Query createQuery(String queryStr) {
        return sessionFactory.getCurrentSession().createQuery(queryStr);
    }

    protected SQLQuery createSQLQuery(String queryStr) {
        return sessionFactory.getCurrentSession().createSQLQuery(queryStr);
    }

    protected Criteria createCriteria(Class<?> persistentClass, String alias) {
        return sessionFactory.getCurrentSession().createCriteria(persistentClass, alias);
    }

    protected <T> T querySingleResult(Query query) {
        @SuppressWarnings("unchecked")
        final List<T> resultList = (List<T>) query.list();
        return toSingleObject(resultList);
    }

    protected <T> T querySingleResult(Criteria criteria) {
        @SuppressWarnings("unchecked")
        final List<T> resultList = (List<T>) criteria.list();
        return toSingleObject(resultList);
    }

    protected <T> T toSingleObject(List<T> resultList) {
        return resultList.isEmpty() ? null : resultList.get(0);
    }

    protected <T> T get(Class<T> c, Serializable id) {
        return c.cast(sessionFactory.getCurrentSession().get(c, id));
    }

    protected void save(Object object) {
        sessionFactory.getCurrentSession().save(object);
    }

    protected void update(Object object) {
        sessionFactory.getCurrentSession().update(object);
    }

    protected void saveOrUpdate(Object object) {
        sessionFactory.getCurrentSession().saveOrUpdate(object);
    }

    protected void delete(Object object) {
        sessionFactory.getCurrentSession().delete(object);
    }

    protected void flush() {
        sessionFactory.getCurrentSession().flush();
    }

}
