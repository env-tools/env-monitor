package org.envtools.monitor.module.querylibrary.viewmapper.impl;

import com.google.common.collect.Maps;
import org.envtools.monitor.model.querylibrary.db.Category;
import org.envtools.monitor.model.querylibrary.tree.view.CategoryView;
import org.envtools.monitor.module.querylibrary.viewmapper.CategoryViewMapper;

import java.util.Map;


/**
 * Created: 01.04.16 22:05
 *
 * @author Yury Yakovlev
 */
public class DefaultCategoryViewMapper implements CategoryViewMapper {

    @Override
    public Map<String, CategoryView> mapCategoriesByOwner(Map<String, Category> categoriesByOwner) {
        //TODO implement
        Map<String, CategoryView> view = Maps.newLinkedHashMap();
        for (Map.Entry<String, Category> entry : categoriesByOwner.entrySet()) {
            view.put(entry.getKey(), mapList(entry.getValue()));
        }
        return view;
    }

    private CategoryView mapList(Category categoryByOwner) {
        return null;
    }
}
