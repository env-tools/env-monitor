package org.envtools.monitor.module.querylibrary.services.bootstrap;

import org.apache.log4j.Logger;
import org.envtools.monitor.model.querylibrary.QueryParamType;
import org.envtools.monitor.model.querylibrary.db.LibQuery;
import org.envtools.monitor.model.querylibrary.db.QueryParam;
import org.envtools.monitor.module.querylibrary.PersistenceTestApplication;
import org.envtools.monitor.module.querylibrary.dao.CategoryDao;
import org.envtools.monitor.module.querylibrary.dao.LibQueryDao;
import org.envtools.monitor.module.querylibrary.dao.QueryParamDao;
import org.envtools.monitor.module.querylibrary.services.BootstrapService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by IAvdeev on 11.01.2017.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = { PersistenceTestApplication.class })
@TestPropertySource(locations = {
        "classpath:/persistence/application-persistence-test.properties",
        "classpath:/querylibrary/bootstrap/bootstrap-test.properties"
})
@Transactional
public class ZipArchiveBootstrapServiceIT {

    private static final Logger LOGGER = Logger.getLogger(ZipArchiveBootstrapService.class);

    // Autowired ZipArchiveBootstrapService, because 'querylibrary.query_bootstrap_location' property ends with '.zip'
    @Autowired
    BootstrapService zipBootstrapService;

    @Autowired
    CategoryDao categoryDao;

    @Autowired
    LibQueryDao queryDao;
    @Autowired
    QueryParamDao queryParamDao;


    @Test
    public void testCategories() throws Exception {
        zipBootstrapService.bootstrap();

        Assert.assertEquals(1, categoryDao.getRootCategories().size());
        Assert.assertEquals(1, categoryDao.getCategoryByTitle("Product Lines").size());
    }

    @Test
    public void testQueryByFilename() throws Exception {
        zipBootstrapService.bootstrap();

        List<LibQuery> libQueryByTextFragment = queryDao.getLibQueryByTextFragment("SELECT * FROM PRODUCT_LINES");

        Assert.assertEquals(1, libQueryByTextFragment.size());

        LibQuery allProductLinesQuery = libQueryByTextFragment.get(0);

        Assert.assertEquals("All Product Lines", allProductLinesQuery.getTitle());
        Assert.assertEquals("All Product Lines", allProductLinesQuery.getDescription());
    }

    @Test
    public void testQueryTitleOverride() throws Exception {
        zipBootstrapService.bootstrap();

        List<LibQuery> libQueryComplexList = queryDao.getLibQueryByTextFragment("SELECT * FROM PRODUCTS WHERE QUANTITY_IN_STOCK > :quantity AND BUY_PRICE > :price");

        Assert.assertEquals(1, libQueryComplexList.size());

        LibQuery complexQuery = libQueryComplexList.get(0);
        Assert.assertEquals("In Stock > Any quantity", complexQuery.getTitle());
        Assert.assertEquals("Some description with -- characters", complexQuery.getDescription());


        Assert.assertEquals(2, complexQuery.getQueryParams().size());

        QueryParam firstParam = complexQuery.getQueryParams().get(0);
        Assert.assertEquals("price", firstParam.getName());
        Assert.assertEquals("1.12", firstParam.getDefaultValue());
        Assert.assertEquals(QueryParamType.STRING, firstParam.getType());

        QueryParam secondParam = complexQuery.getQueryParams().get(1);
        Assert.assertEquals(null, secondParam.getDefaultValue());
        Assert.assertEquals("SELECT * FROM PRODUCTS WHERE QUANTITY_IN_STOCK > :quantity AND BUY_PRICE > :price", complexQuery.getText());

    }
}
