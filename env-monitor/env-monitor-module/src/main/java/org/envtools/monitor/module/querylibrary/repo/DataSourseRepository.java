package org.envtools.monitor.module.querylibrary.repo;

import org.envtools.monitor.model.querylibrary.db.DataSource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by anastasiya on 07.03.16.
 */
@Repository
public interface DataSourseRepository extends JpaRepository<DataSource, Long> {
}
