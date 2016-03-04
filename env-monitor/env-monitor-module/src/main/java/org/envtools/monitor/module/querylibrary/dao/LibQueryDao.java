package org.envtools.monitor.module.querylibrary.dao;

import org.envtools.monitor.model.querylibrary.db.LibQuery;

import java.util.List;

/**
 * Created: 05.03.16 1:55
 *
 * @author Yury Yakovlev
 */
public interface LibQueryDao extends Dao<LibQuery, Long> {

    List<LibQuery> getLibQueryByTextFragment(String textFragment);

}
