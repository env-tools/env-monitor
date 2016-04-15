package org.envtools.monitor.module.querylibrary.services;

import org.envtools.monitor.model.querylibrary.updates.DataOperation;

import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;

/**
 * Created: 11.04.16 15:59
 *
 * @author Anastasiya Plotnikova
 */
public interface DataOperationProcessor {
    DataOperationResult process(DataOperation operation) throws NoSuchMethodException, IntrospectionException, IllegalAccessException, InstantiationException, InvocationTargetException, ClassNotFoundException;
}
