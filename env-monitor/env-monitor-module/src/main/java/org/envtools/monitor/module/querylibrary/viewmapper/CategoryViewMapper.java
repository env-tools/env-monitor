package org.envtools.monitor.module.querylibrary.viewmapper;

import org.envtools.monitor.model.querylibrary.db.Category;
import org.envtools.monitor.model.querylibrary.tree.view.CategoryView;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Created: 01.04.16 23:53
 *
 * @author Yury Yakovlev
 */
public interface CategoryViewMapper {

    Map<String, List<CategoryView>> mapCategoriesByOwner(Map<String, List<Category>> categoriesByOwner);

    Map<String, String> mapCategoriesByOwnerToString(Map<String, List<CategoryView>> categoriesByOwner) throws IOException;

}
