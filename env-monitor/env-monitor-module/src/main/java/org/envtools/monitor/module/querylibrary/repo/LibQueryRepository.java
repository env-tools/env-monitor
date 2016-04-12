package org.envtools.monitor.module.querylibrary.repo;

import org.envtools.monitor.model.querylibrary.db.LibQuery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Yury Yakovlev
 */
@Repository
public interface LibQueryRepository extends JpaRepository<LibQuery, Long> {
}

//Does not work as of now
