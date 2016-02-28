package org.envtools.monitor.module.querylibrary.repo;

import org.envtools.monitor.model.querylibrary.db.LibQuery;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author Yury Yakovlev
 */
public interface LibQueryRepository extends JpaRepository<LibQuery, Long> {
}
