package org.envtools.monitor.model.updates;

import java.util.Map;

/**
 * Created by jesa on 18.03.2016.
 */
public class DataOperation {
    enum DataOperationType {CREATE,UPDATE,DELETE}; //available types of operations
    String entity;
    Map<String,String> fields;
}
