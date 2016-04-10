package org.envtools.monitor.module;

import org.envtools.monitor.model.querylibrary.execution.QueryExecutionResult;
import org.envtools.monitor.model.updates.DataOperation;

import java.lang.reflect.InvocationTargetException;

/**
 * Created: 10.04.16 12:21
 *
 * @author Anastasiya Plotnikova
 */
public interface DataOperationInterface {
    void dataOperations(DataOperation dataOperation);
    QueryExecutionResult create(DataOperation dataOperation) throws ClassNotFoundException, InvocationTargetException, IllegalAccessException, NoSuchMethodException, InstantiationException, NoSuchFieldException;
    void update(Long id,DataOperation dataOperation);
    void delete(Long id,DataOperation dataOperation);

}
