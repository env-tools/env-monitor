package org.envtools.monitor.module.querylibrary.services;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.Logger;
import org.envtools.monitor.model.querylibrary.execution.QueryExecutionResult;
import org.envtools.monitor.model.updates.DataOperation;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * Created: 10.04.16 12:18
 *
 * @author Anastasiya Plotnikova
 */
@Service
public class DataOperationServiceImpl extends DataOperation implements DataOperationService<Long> {

    private static final Logger LOGGER = Logger.getLogger(DataOperationServiceImpl.class);
    private static final String ID = "_id";
    private static final String path = "org.envtools.monitor.model.querylibrary.db.";


    @Override
    public DataOperationResult create(String entity, Map<String, String> fields) throws
            IllegalAccessException, NoSuchMethodException, InvocationTargetException, ClassNotFoundException, IntrospectionException {

        Map<String, Object> result = new HashMap<String, Object>();
        BeanInfo info;
        Class entityClass = Class.forName(path + entity);
        BeanInfo bi = Introspector.getBeanInfo(entityClass);
        PropertyDescriptor[] pds = bi.getPropertyDescriptors();
        for(int i=0;i<pds.length;i++){
            String propName = pds[i].getDisplayName();
            Method propName1 = pds[i].getReadMethod();
            ;
            LOGGER.info("Name  "+propName);
            LOGGER.info("ReadMethod  "+propName1.getName());
            LOGGER.info("WriteMethod  "+pds[i].getWriteMethod());
            LOGGER.info("PropertyEditorClass  "+pds[i].getPropertyType().getName());
            //PropertyDescriptor descriptor =pds[i];
           // LOGGER.info("ExceptionTypesReadMethod  "+pds[i].getReadMethod().getExceptionTypes().toString());
        }

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
}
