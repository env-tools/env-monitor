package org.envtools.monitor.module.querylibrary.repo;

import org.envtools.monitor.model.querylibrary.db.DataSourceProperty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created: 07.03.16 21:32
 *
 * @author Anastasiya Plotnikova
 */
@Repository
public interface DataSourcePropertiesRepository  extends JpaRepository<DataSourceProperty, Long> {
}
