package org.envtools.monitor.module.querylibrary.viewmapper.impl;

import org.envtools.monitor.model.querylibrary.db.Category;
import org.envtools.monitor.model.querylibrary.tree.view.CategoryView;
import org.envtools.monitor.module.querylibrary.viewmapper.CategoryViewMapper;

import java.util.Map;

/**
 * Created: 01.04.16 22:05
 *
 * @author Yury Yakovlev
 */
public class DefaultCategoryViewMapper implements CategoryViewMapper{

    @Override
    public Map<String, CategoryView> mapCategoriesByOwner(Map<String, Category> categoriesByOwner) {
        //TODO implement
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
