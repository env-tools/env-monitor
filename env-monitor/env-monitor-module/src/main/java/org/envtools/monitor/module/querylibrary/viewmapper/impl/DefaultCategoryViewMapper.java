package org.envtools.monitor.module.querylibrary.viewmapper.impl;

import org.envtools.monitor.model.querylibrary.db.Category;
import org.envtools.monitor.model.querylibrary.tree.view.CategoryView;
import org.envtools.monitor.module.querylibrary.viewmapper.CategoryViewMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created: 01.04.16 22:05
 *
 * @author Yury Yakovlev
 */
@Repository
public class DefaultCategoryViewMapper implements CategoryViewMapper{

    @Override
    public Map<String, List<CategoryView>> mapCategoriesByOwner(Map<String, List<Category>> categoriesByOwner) {
        //TODO implement
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Map<String, String> mapCategoriesByOwnerToString(Map<String, CategoryView> categoriesByOwner) {
        return null;
    }
}
