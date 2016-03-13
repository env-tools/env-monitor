package org.envtools.monitor.module.querylibrary.repo;

import org.envtools.monitor.model.querylibrary.db.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by anastasiya on 06.03.16.
 */

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
}
