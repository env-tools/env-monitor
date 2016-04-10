package org.envtools.monitor.module;

import org.envtools.monitor.model.querylibrary.execution.QueryExecutionResult;

import java.lang.reflect.InvocationTargetException;

/**
 * Created: 10.04.16 12:21
 *
 * @author Anastasiya Plotnikova
 */
public interface DataOperationInterface {
    QueryExecutionResult create(String entity) throws ClassNotFoundException, InvocationTargetException, IllegalAccessException, NoSuchMethodException, InstantiationException;
    void update(Long id,String entity);
    void delete(Long id,String entity);

}
