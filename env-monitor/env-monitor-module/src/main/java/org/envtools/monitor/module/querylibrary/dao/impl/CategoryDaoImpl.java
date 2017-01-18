package org.envtools.monitor.module.querylibrary.dao.impl;

import org.apache.log4j.Logger;
import org.envtools.monitor.model.querylibrary.db.Category;
import org.envtools.monitor.model.querylibrary.db.LibQuery;
import org.envtools.monitor.module.querylibrary.dao.CategoryDao;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by anastasiya on 06.03.16.
 */
@Repository
public class CategoryDaoImpl extends AbstractDbDao<Category, Long> implements CategoryDao {


    private static final Logger LOGGER = Logger.getLogger(CategoryDaoImpl.class);

    public CategoryDaoImpl() {

        setClazz(Category.class);

        LOGGER.info("CategoryDaoImpl created.");
    }

    @Override
    public List<Category> getCategoryByTitle(String title) {
        return em.createQuery("FROM Category WHERE title LIKE :titleParam")
                .setParameter("titleParam", createPatternString(title))
                .getResultList();
    }

    @Override
    public List<Category> getRootCategories() {
        List<Category> resultList = em.createQuery("FROM Category WHERE parentCategory=null").getResultList();
        return resultList;
    }

    @Override
    public List<Category> getChildCategoriesByParentId(Long id) {
        return em.createQuery("FROM Category childCategories WHERE parentCategory<>null and  parentCategory.id =:param")
                .setParameter("param", id)
                .getResultList();
    }

    private String createPatternString(String text) {
        return "%" + text + "%";
    }
}
