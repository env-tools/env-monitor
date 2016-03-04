package org.envtools.monitor.module.querylibrary.repo;

import org.envtools.monitor.model.querylibrary.db.LibQuery;
import org.springframework.data.repository.CrudRepository;

/**
 *
 * @author Yury Yakovlev
 */
public interface LibQueryRepository extends CrudRepository<LibQuery, Long> {
}
