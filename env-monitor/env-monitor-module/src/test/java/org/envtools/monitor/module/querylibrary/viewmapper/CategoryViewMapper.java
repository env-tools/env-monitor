package org.envtools.monitor.module.querylibrary.viewmapper;

import org.envtools.monitor.model.querylibrary.db.Category;
import org.envtools.monitor.model.querylibrary.tree.view.CategoryView;

import java.util.Map;

/**
 * Created: 01.04.16 23:53
 *
 * @author Yury Yakovlev
 */
public interface CategoryViewMapper {

   Map<String, CategoryView> mapCategoriesByOwner(Map<String, Category> categoriesByOwner);

}
