package org.envtools.monitor.module.querylibrary.dao;

import org.hibernate.SessionFactory;

/**
 * Created: 23.02.16 3:01
 *
 * @author Yury Yakovlev
 */
public class LibQueryDao extends AbstractDbDao{

    public LibQueryDao(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

}
