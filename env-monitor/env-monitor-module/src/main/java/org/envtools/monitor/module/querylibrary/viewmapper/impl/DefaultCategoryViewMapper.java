package org.envtools.monitor.module.querylibrary.viewmapper.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.envtools.monitor.model.querylibrary.db.*;
import org.envtools.monitor.model.querylibrary.tree.view.*;
import org.envtools.monitor.module.querylibrary.viewmapper.CategoryViewMapper;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created: 01.04.16 22:05
 *
 * @author Yury Yakovlev
 */
@Repository
public class DefaultCategoryViewMapper implements CategoryViewMapper {

    @Override
    public Map<String, List<CategoryView>> mapCategoriesByOwner(Map<String, List<Category>> categoriesByOwner,
                                                                List<DataSource> dataSources) {
        Map<String, List<CategoryView>> view = Maps.newLinkedHashMap();

        List<DataSourceView> dataSourceViews = mapDataSources(dataSources);

        for (Map.Entry<String, List<Category>> entry : categoriesByOwner.entrySet()) {
            view.put(entry.getKey(), mapList(entry.getValue(), dataSourceViews));
        }

        return view;
    }

    private List<DataSourceView> mapDataSources(List<DataSource> dataSources) {
        return dataSources.stream().map(dataSource ->
                new DataSourceView(
                    String.valueOf(dataSource.getType()),
                    dataSource.getName(),
                    dataSource.getDescription(),
                    dataSource
                            .getDataSourceProperties()
                            .stream()
                            .collect(Collectors.toMap(
                                    DataSourceProperty::getProperty,
                                    DataSourceProperty::getValue))
                    )
            ).collect(Collectors.<DataSourceView>toList());
    }

    private List<CategoryView> mapList(List<Category> categoriesByOwner,
                                       List<DataSourceView> dataSourceViews) {
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
                            parameterValues,
                            dataSourceViews));
                }
                categoryView.setQueries(queryViewList);
            }
            if (entry.getChildCategories() != null) {
                categoryView.setChildCategories(mapList(entry.getChildCategories(), dataSourceViews));
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
