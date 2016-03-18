package org.envtools.monitor.module.querylibrary.dao;

import org.envtools.monitor.model.querylibrary.db.Category;

import java.util.List;

/**
 * Created by anastasiya on 06.03.16.
 */
public interface CategoryDao   extends Dao<Category, Long> {
    List<Category> getCategoryByTitle(String title);
    List<Category> getRootCategories();

}
