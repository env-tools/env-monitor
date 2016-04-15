package org.envtools.monitor.module.querylibrary.services.impl.updates;

import org.envtools.monitor.model.querylibrary.updates.DataOperation;
import org.envtools.monitor.module.querylibrary.services.DataOperationProcessor;
import org.envtools.monitor.module.querylibrary.services.DataOperationResult;
import org.envtools.monitor.module.querylibrary.services.DataOperationService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

/**
 * Created: 11.04.16 16:05
 *
 * @author Anastasiya Plotnikova
 */
public class DataOperationProcessorImpl implements DataOperationProcessor {

    @Autowired
    DataOperationService<Long> dataOperationService;

    @Override
    public DataOperationResult process(DataOperation operation) {
        return null;
    }
}
