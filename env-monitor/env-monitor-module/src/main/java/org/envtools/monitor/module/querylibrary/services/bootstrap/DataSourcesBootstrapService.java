package org.envtools.monitor.module.querylibrary.services.bootstrap;


import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.envtools.monitor.model.querylibrary.db.DataSource;
import org.envtools.monitor.module.querylibrary.dao.DataSourceDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * Reads a list of datasources available for queries from yaml file and saves it to database.
 */
@Service
@Transactional
public class DataSourcesBootstrapService {

    private Resource yamlSource;

    private DataSourceDao dataSourceDao;

    @Autowired
    public DataSourcesBootstrapService(@Value("${querylibrary.data_sources_bootstrapper.location:}") Resource yamlSource,
                                       DataSourceDao dataSourceDao) {
        this.yamlSource = yamlSource;
        this.dataSourceDao = dataSourceDao;
    }

    /**
     * Reads an yaml file with datasources and saves it to database
     *
     * @throws IOException when there is problem reading yaml file
     * @throws ConstraintViolation when there is missing properties for datasource
     */
    public void bootstrapDataSources() throws IOException {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        mapper.configure(DeserializationFeature.FAIL_ON_MISSING_CREATOR_PROPERTIES, true);
        DataSource[] sources = mapper.readValue(yamlSource.getFile(), DataSource[].class);

        dataSourceDao.save(Arrays.asList(sources));
    }
}
