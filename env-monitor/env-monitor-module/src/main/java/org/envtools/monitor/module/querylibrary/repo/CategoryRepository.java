package org.envtools.monitor.module.querylibrary.repo;

import org.envtools.monitor.model.querylibrary.db.Category;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by anastasiya on 06.03.16.
 */
public interface CategoryRepository extends JpaRepository<Category, Long> {
}
