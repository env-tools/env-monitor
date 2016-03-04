package org.envtools.monitor.module.querylibrary.dao.impl;

import org.apache.log4j.Logger;
import org.envtools.monitor.model.querylibrary.db.LibQuery;
import org.envtools.monitor.module.querylibrary.dao.LibQueryDao;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created: 23.02.16 3:01
 *
 * @author Yury Yakovlev
 */
@Repository
public class LibQueryDaoImpl  extends AbstractDbDao<LibQuery, Long> implements LibQueryDao {

    private static final Logger LOGGER = Logger.getLogger(LibQueryDaoImpl.class);

    public LibQueryDaoImpl() {

        setClazz(LibQuery.class);

        LOGGER.info("LibQueryDaoImpl created.");
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<LibQuery> getLibQueryByTextFragment(String textFragment) {
        return em.createQuery("FROM LibQuery WHERE text LIKE :textFragmentParam")
                .setParameter("textFragmentParam", createPatternString(textFragment))
                .getResultList();
    }

    private String createPatternString(String text) {
        return "%" + text + "%";
    }


}
