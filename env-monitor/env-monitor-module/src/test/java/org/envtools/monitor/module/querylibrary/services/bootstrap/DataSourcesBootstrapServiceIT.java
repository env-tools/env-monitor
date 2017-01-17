package org.envtools.monitor.module.querylibrary.services.bootstrap;

import org.envtools.monitor.model.querylibrary.DataProviderType;
import org.envtools.monitor.model.querylibrary.db.DataSource;
import org.envtools.monitor.model.querylibrary.db.DataSourceProperty;
import org.envtools.monitor.module.querylibrary.PersistenceTestApplication;
import org.envtools.monitor.module.querylibrary.dao.DataSourceDao;
import org.envtools.monitor.module.querylibrary.dao.LibQueryDao;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.transaction.Transactional;
import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by IAvdeev on 16.01.2017.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = { PersistenceTestApplication.class })
@TestPropertySource(locations = {"classpath:/persistence/application-persistence-test.properties"})
@Transactional
public class DataSourcesBootstrapServiceIT {

    @Autowired
    DataSourceDao dataSourceDao;

    @Autowired
    LibQueryDao queryDao;

    @Test
    public void testBootstrapDataSources() throws Exception {
        DataSourcesBootstrapService service = new DataSourcesBootstrapService(new ClassPathResource("querylibrary/bootstrap/test-data-sources.yaml"), dataSourceDao);
        service.bootstrapDataSources();

        Assert.assertEquals(2, dataSourceDao.findAll().size());

        DataSource h2 = dataSourceDao.getByName("H2");

        Map<String, String> h2Properties = h2.getDataSourceProperties()
                .stream()
                .collect(Collectors.toMap(DataSourceProperty::getProperty, DataSourceProperty::getValue));

        Assert.assertEquals("H2", h2.getName());
        Assert.assertEquals("H2 data source", h2.getDescription());
        Assert.assertEquals(DataProviderType.JDBC, h2.getType());
        Assert.assertEquals("user", h2Properties.get("username"));
        Assert.assertEquals("secret", h2Properties.get("password"));
    }

    @Test(expected = ConstraintViolationException.class)
    public void testInvalidBootstrapDataSources() throws Exception {
        DataSourcesBootstrapService service = new DataSourcesBootstrapService(new ClassPathResource("querylibrary/bootstrap/test-data-sources-invalid.yaml"), dataSourceDao);
        service.bootstrapDataSources();
    }
}
