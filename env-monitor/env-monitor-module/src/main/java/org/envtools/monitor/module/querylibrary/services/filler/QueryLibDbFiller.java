package org.envtools.monitor.module.querylibrary.services.filler;

import org.apache.log4j.Logger;

import org.envtools.monitor.model.querylibrary.DataProviderType;
import org.envtools.monitor.model.querylibrary.db.Category;
import org.envtools.monitor.model.querylibrary.db.LibQuery;
import org.envtools.monitor.model.querylibrary.db.QueryParam;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by sergey on 14.03.2016.
 */
@Transactional
public class QueryLibDbFiller {

    private static final Logger LOGGER = Logger.getLogger(QueryLibDbFiller.class);

    public static void fillDatabase(EntityManager em, boolean createSampleTestHistory) {
        LOGGER.info("Start to fill database");
        List<Category> publicCategoryList = new ArrayList<>();
        List<Category> privateCategoryList = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            publicCategoryList.add(publicCategoryFactory());
            privateCategoryList.add(privateCategoryFactory());
        }

        publicCategoryList.get(1).setParentCategory(publicCategoryList.get(0));
        publicCategoryList.get(2).setParentCategory(publicCategoryList.get(0));
        publicCategoryList.get(3).setParentCategory(publicCategoryList.get(2));

        privateCategoryList.get(1).setParentCategory(privateCategoryList.get(0));
        privateCategoryList.get(2).setParentCategory(privateCategoryList.get(0));
        privateCategoryList.get(3).setParentCategory(privateCategoryList.get(2));

        saveCategories(em, privateCategoryList);
        saveCategories(em, publicCategoryList);

        em.flush();
        LOGGER.info("End to fill database");
    }

    public static void saveCategories(EntityManager em, List<Category> categories) {
        for (Category category: categories) {
            LibQuery query = queryFactory(category);
            QueryParam queryParam = queryParamFactory(query);
            em.persist(category);
            em.persist(query);
            em.persist(queryParam);
        }
    }

    public static Category publicCategoryFactory() {
        Category category = new Category();

        category.setTitle("Title of " + category.hashCode() + " category.");
        category.setDescription("Desc of " + category.hashCode() + " category.");

        return category;
    }

    public static Category privateCategoryFactory() {
        Category category = publicCategoryFactory();

        category.setOwner(UUID.randomUUID().toString());

        return category;
    }

    public static LibQuery queryFactory(Category category) {
        LibQuery query = new LibQuery();

        query.setTitle("Title of " + query.hashCode() + " query.");
        query.setDescription("Desc of " + query.hashCode() + " query.");
        query.setText("SELECT * FROM LIB_QUERY WHERE id = :id");
        query.setCategory(category);

        return query;
    }

    public static QueryParam queryParamFactory(LibQuery query) {
        QueryParam queryParam = new QueryParam();

        queryParam.setName(":id");
        queryParam.setLibQuery(query);
        queryParam.setType(DataProviderType.JDBC);

        return queryParam;
    }
}
