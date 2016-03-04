package org.envtools.monitor.module.querylibrary.dao.impl;

import com.google.common.collect.Lists;
import edu.umd.cs.findbugs.annotations.Nullable;
import org.apache.log4j.Logger;
import org.envtools.monitor.module.querylibrary.dao.Dao;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.Serializable;
import java.util.List;

/**
 * Created: 2/23/16 12:48 AM
 *
 * @author Yury Yakovlev
 */
public abstract class AbstractDbDao<T, ID extends Serializable> implements Dao<T, ID> {

    private static final Logger LOGGER = Logger.getLogger(AbstractDbDao.class);

    @PersistenceContext
    protected EntityManager em;

    protected Class<T> clazz;

    @PostConstruct
    public void init() {
       LOGGER.info("Initializing AbstractDbDao, using entityManager : " + em);
    }

    public void setClazz( Class< T > clazzToSet ){
        this.clazz = clazzToSet;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<T> findAll(Iterable<ID> ids) {
       List<T> result = Lists.newArrayList();
        for (ID id : ids) {
            T entity = em.find(clazz, id);
            if (entity != null) {
               result.add(entity);
            }
        }
        return result;
    }

    @Override
    public <S extends T> List<S> save(Iterable<S> entities) {

        List<S> result = Lists.newArrayList();

        for (S entity : entities) {
            em.persist(entity);
            result.add(entity);
        }
        return result;
    }

    @Override
    public void flush() {
        em.flush();
    }

    @Override
    public <S extends T> S saveAndFlush(S entity) {
        em.persist(entity);
        em.flush();
        return entity;
    }

    @Override
    public <S extends T> S save(S entity) {
        em.persist(entity);
        return entity;
    }

    @Override
    public void delete(T entity) {
        em.remove(entity);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<T> findAll() {
        return em.createQuery( "from " + clazz.getName() )
                .getResultList();
    }

    @Override
    public void deleteById(ID id) {
        T entity = em.find(clazz, id);
        if (entity != null) {
            em.remove(entity);
        }
    }

    @Override
    @Nullable
    public T getOne(ID id) {
        return em.find(clazz, id);
    }


}
