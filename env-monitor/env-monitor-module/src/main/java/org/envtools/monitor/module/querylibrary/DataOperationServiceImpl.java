package org.envtools.monitor.module.querylibrary;

import org.apache.log4j.Logger;
import org.envtools.monitor.model.querylibrary.execution.QueryExecutionResult;
import org.envtools.monitor.model.updates.DataOperation;
import org.envtools.monitor.module.DataOperationInterface;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

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
public class DataOperationServiceImpl extends DataOperation implements DataOperationInterface {

    private static final Logger LOGGER = Logger.getLogger(DataOperationServiceImpl.class);
    private static final String ID = "_id";
    private static final String path = "org.envtools.monitor.model.querylibrary.db.";

    @Override
    public void dataOperations(DataOperation dataOperation) {
        /*
        TODO взависимости от типа выбираем операцию, реализую позже
         */
    }

    @Override
    public QueryExecutionResult create(DataOperation dataOperation) throws ClassNotFoundException, InvocationTargetException, IllegalAccessException, NoSuchMethodException, InstantiationException, NoSuchFieldException {
        String entity = dataOperation.getEntity();
        /* Найдем все поля, имя которых заканчивается на "_id" */
        Map<String, String> fields = dataOperation.getFields();

        //получаем все ключи, необходимые для нахождения свойств
        List<String> keyFields = new ArrayList<String>((fields.keySet()));

        //получаем все значения
        List<String> valuesFields = new ArrayList<String>((fields.values()));

        List<String> propertyID=new ArrayList<String>();

        List<String> property=new ArrayList<String>();

        //Содержит все методы и их возвр значения
        Map<Method,Class[]> propertyDataID=new HashMap<Method,Class[]>();
        Map<Method,Class[]> propertyData=new HashMap<Method,Class[]>();
        for (int i = 0; i < keyFields.size(); i++) {
            if (keyFields.get(i).endsWith(ID)) {
                keyFields.set(i, keyFields.get(i).replace(ID, ""));
                propertyID.add(keyFields.get(i));
            }
            else{
                property.add(keyFields.get(i));
            }
            LOGGER.info("field:  " + keyFields.get(i));
            LOGGER.info("field value:  " + valuesFields.get(i));

        }
        Class entityClass = Class.forName(path + entity);
        Field field;
        //Object target;
       /*TODO Научиться получать методы и их входные параметры*/
       for(int i = 0; i <propertyID.size(); i++) {
           Method[] methods = entityClass.getMethods();
           for (Method method : methods) {
               System.out.println("Имя: " + method.getName());
               System.out.println("Возвращаемый тип: " + method.getReturnType().getName());

               Class[] paramTypes = method.getParameterTypes();
               System.out.print("Типы параметров: ");
               for (Class paramType : paramTypes) {
                   System.out.print(" " + paramType.getName());
               }
               System.out.println();
           }
       }

        for(int i = 0; i <property.size(); i++){
            field= ReflectionUtils.findField(entityClass,"set" + property.get(i).
                    replaceFirst(property.get(i).substring(0, 1), property.get(i)
                            .substring(0, 1).toUpperCase()));
        }


        LOGGER.info("Полученный класс:  " + entityClass.getName());
        return null;
    }

    @Override
    public void update(Long id, DataOperation dataOperation) {

    }

    @Override
    public void delete(Long id, DataOperation dataOperation) {

    }
}
