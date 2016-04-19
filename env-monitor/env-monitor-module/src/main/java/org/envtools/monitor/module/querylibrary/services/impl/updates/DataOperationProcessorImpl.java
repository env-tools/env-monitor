package org.envtools.monitor.module.querylibrary.services.impl.updates;

import org.envtools.monitor.model.querylibrary.updates.DataOperation;
import org.envtools.monitor.model.querylibrary.updates.DataOperationType;
import org.envtools.monitor.module.exception.DataOperationException;
import org.envtools.monitor.module.querylibrary.services.DataOperationProcessor;
import org.envtools.monitor.module.querylibrary.services.DataOperationResult;
import org.envtools.monitor.module.querylibrary.services.DataOperationService;
import org.springframework.beans.factory.annotation.Autowired;

import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
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
    public DataOperationResult process(DataOperation operation) throws NoSuchMethodException, IntrospectionException, IllegalAccessException, InstantiationException, InvocationTargetException, ClassNotFoundException, DataOperationException {

        switch (operation.getType()) {
            case CREATE:
                return dataOperationService.create(operation.getEntity(), operation.getFields());
            case UPDATE:
                return dataOperationService.update(operation.getEntity(), operation.getId(), operation.getFields());
            case DELETE:
                return dataOperationService.delete(operation.getEntity(), operation.getId());
            default:
                return DataOperationResult.builder().status(DataOperationResult.DataOperationStatusE.ERROR).build();
        }

    }
}
