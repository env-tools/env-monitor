package org.envtools.monitor.module.querylibrary.services.impl.updates;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.Logger;
import org.envtools.monitor.model.updates.DataOperation;
import org.envtools.monitor.module.querylibrary.services.DataOperationResult;
import org.envtools.monitor.module.querylibrary.services.DataOperationService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.UserTransaction;
import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * Created: 10.04.16 12:18
 *
 * @author Anastasiya Plotnikova
 */
@Service
public class DataOperationServiceImpl implements DataOperationService<Long> {

    private static final Logger LOGGER = Logger.getLogger(DataOperationServiceImpl.class);
    private static final String ID = "_id";
    private static final String STRING = "String";
    private static final String path = "org.envtools.monitor.model.querylibrary.db.";

    //@Resource
    UserTransaction ut;
    @PersistenceContext
    EntityManager entityManager;

    @Override
    public DataOperationResult create(String entity, Map<String, String> fields) throws
            IllegalAccessException, NoSuchMethodException, InvocationTargetException, ClassNotFoundException, IntrospectionException {

        Map<String, Object> result = new HashMap<String, Object>();
        Map<String, Object> resultId = new HashMap<String, Object>();
        List<String> propertyId = new ArrayList<String>();
        List<String> property = new ArrayList<String>();

        /*Найдем все string, которые заканчиваются на _id*/
        List<String> list = new ArrayList(fields.keySet());

        for (int i = 0; i < fields.size(); i++) {
            if (list.get(i).contains(ID)) {
                propertyId.add(list.get(i).replace(ID, ""));
                //  LOGGER.info("есть ID "+list.get(i));

            } else {
                property.add(list.get(i));
                //   LOGGER.info("not ID "+list.get(i));
            }
        }

        //находим класс по entity
        Class entityClass = Class.forName(path + entity);
        BeanInfo bi = Introspector.getBeanInfo(entityClass);
        PropertyDescriptor[] pds = bi.getPropertyDescriptors();

        /*Находим все методы setXXX, которые содержат в названии*/
        for (int i = 0; i < pds.length; i++) {

            if (pds[i].getWriteMethod() != null) {

                /*находим методы с id в классе entity*/
                for (int j = 0; j < propertyId.size(); j++) {
                    if (pds[i].getWriteMethod().getName().contains(propertyId.get(j))) {
                        LOGGER.info("есть ID в методе " + pds[i].getWriteMethod() + "    " + propertyId.get(j));
                        LOGGER.info("Метод принимает " + Arrays.asList(pds[i].getWriteMethod().getParameterTypes()));
                        Class[] clazz = pds[i].getWriteMethod().getParameterTypes();
                        //BeanUtils.
                     /*
                     * Узнали какие параметры принимает наш метод.
                     * Теперь необходимо достать связный объект
                     * */

                        for (int k = 0; k < clazz.length; k++) {
                            LOGGER.info("Тип параметра принимаемого методом " + Class.forName(clazz[k].getName()));
                            resultId.put(propertyId.get(j), entityManager.find(Class.forName(clazz[k].getName()),
                                    Long.valueOf(fields.get(propertyId.get(j) + ID))));
                            LOGGER.info("Object - связный объект" + resultId.get(propertyId.get(j)));
                        }
                    }

                }

                for (int g = 0; g < property.size(); g++) {

                    if (pds[i].getWriteMethod().getName().contains(property.get(g))) {
                        LOGGER.info("not ID в методе " + pds[i].getWriteMethod() + "    " + property.get(g));
                        Class[] clazz1 = pds[i].getWriteMethod().getParameterTypes();

                        for (int k = 0; k < clazz1.length; k++) {

                            Object object = null;
                            object = Class.forName(clazz1[k].getName());
                            if (object.toString().contains(STRING)) {
                                fields.put(property.get(g),
                                        String.valueOf(fields.get(property.get(g))));
                                LOGGER.info("Метод  " + property.get(g) +
                                        " строка " + fields.get(property.get(g)) + "   " + fields.get(property.get(g)).getClass());

                            } else {
                               LOGGER.info("Другой тип "+object);
                            }
                            //result.put(property.get(g), object);
                            // LOGGER.info("Object - связный объект" + result.get(property.get(g)));
                        }

                    }


                }

            }

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
