package org.envtools.monitor.module.querylibrary.services.impl;

import org.envtools.monitor.model.querylibrary.updates.DataOperation;
import org.envtools.monitor.module.querylibrary.services.DataOperationProcessor;
import org.envtools.monitor.module.querylibrary.services.DataOperationResult;
import org.envtools.monitor.module.querylibrary.services.DataOperationService;

import java.util.Map;

/**
 * Created: 11.04.16 16:05
 *
 * @author Anastasiya Plotnikova
 */
public class DataOperationProcessorImpl implements DataOperationProcessor, DataOperationService<Long> {
    @Override
    public DataOperationResult process(DataOperation operation) {
        return null;
    }

    @Override
    public DataOperationResult create(String entity, Map<String, String> fields) {
        return null;
    }

    @Override
    public DataOperationResult update(String entity, Long id, Map<String, String> fields) {
        return null;
    }

    @Override
    public DataOperationResult delete(String entity, Long id) {
        return null;
    }

    //DataOperationService<Long>
}
