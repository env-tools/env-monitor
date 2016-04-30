package org.envtools.monitor.module.querylibrary.viewmapper.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.codehaus.jackson.map.ObjectMapper;
import org.envtools.monitor.model.querylibrary.db.Category;
import org.envtools.monitor.model.querylibrary.db.LibQuery;
import org.envtools.monitor.model.querylibrary.db.QueryParam;
import org.envtools.monitor.model.querylibrary.tree.view.CategoryView;
import org.envtools.monitor.model.querylibrary.tree.view.ParameterValueSetView;
import org.envtools.monitor.model.querylibrary.tree.view.ParameterView;
import org.envtools.monitor.model.querylibrary.tree.view.QueryView;
import org.envtools.monitor.module.querylibrary.viewmapper.CategoryViewMapper;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created: 01.04.16 22:05
 *
 * @author Yury Yakovlev
 */
@Repository
public class DefaultCategoryViewMapper implements CategoryViewMapper {

    @Override
    public Map<String, List<CategoryView>> mapCategoriesByOwner(Map<String, List<Category>> categoriesByOwner) {
        //TODO implement
        Map<String, List<CategoryView>> view = Maps.newLinkedHashMap();
        for (Map.Entry<String, List<Category>> entry : categoriesByOwner.entrySet()) {
            view.put(entry.getKey(), mapList(entry.getValue()));
        }
        return view;
    }

    private List<CategoryView> mapList(List<Category> categoriesByOwner) {
        List<CategoryView> categoryViews = Lists.newArrayList();

        for (Category entry : categoriesByOwner) {
            Long parentCategoryId = entry.getParentCategory() != null ? entry.getParentCategory().getId() : null;
            CategoryView categoryView = new CategoryView(entry.getId(), entry.getTitle(),
                    entry.getDescription(), parentCategoryId);
            if (entry.getQueries() != null) {
                List<QueryView> queryViewList = Lists.newArrayList();
                for (LibQuery libQuery : entry.getQueries()) {

                    List<ParameterView> parameters = getParameterViews(libQuery);
                    List<ParameterValueSetView> parameterValues = getParameterValueSetViews(libQuery);

                    Long categoryId = libQuery.getCategory() != null ? libQuery.getCategory().getId() : null;
                    queryViewList.add(new QueryView(categoryId,
                            libQuery.getText(),
                            libQuery.getTitle(),
                            libQuery.getDescription(),
                            libQuery.getId(),
                            parameters,
                            parameterValues));
                }
                categoryView.setQueries(queryViewList);
            }
            if (entry.getChildCategories() != null) {
                categoryView.setChildCategories(mapList(entry.getChildCategories()));
            }

            categoryViews.add(categoryView);
        }
        return categoryViews;
    }

    /**
     * Возвращает историю запросов
     * @param libQuery
     * @return
     */
    protected List<ParameterValueSetView> getParameterValueSetViews(LibQuery libQuery) {
        List<ParameterValueSetView> result = new ArrayList<>();

        return result;
    }

    protected List<ParameterView> getParameterViews(LibQuery libQuery) {
        List<ParameterView> result = new ArrayList<>();
        List<QueryParam> queryParams = libQuery.getQueryParams();
        for (QueryParam entry : queryParams) {
            result.add(new ParameterView(entry.getName(), entry.getType().toString()));
        }
        return result;
    }


    @Override
    public Map<String, String> mapCategoriesByOwnerToString(Map<String, List<CategoryView>> categoriesByOwner) throws IOException {
        Map<String, String> view = Maps.newLinkedHashMap();
        for (Map.Entry<String, List<CategoryView>> entry : categoriesByOwner.entrySet()) {
            view.put(entry.getKey(), listToJson(entry.getValue()));
        }
        return view;
    }

    private String listToJson(List<CategoryView> value) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(value);
    }
}
