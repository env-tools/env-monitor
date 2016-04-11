package org.envtools.monitor.module.querylibrary.services;

import java.util.Map;

/**
 * Created: 11.04.16 16:02
 *
 * @author Anastasiya Plotnikova
 */
public interface DataOperationService<T> {

    DataOperationResult create(String entity, Map<String, String> fields);

    DataOperationResult update(String entity, T id, Map<String, String> fields);

    DataOperationResult delete(String entity, T id);
}
