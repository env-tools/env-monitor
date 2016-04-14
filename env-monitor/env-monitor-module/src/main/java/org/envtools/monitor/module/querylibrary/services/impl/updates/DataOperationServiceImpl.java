package org.envtools.monitor.module.querylibrary.services.impl.updates;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.Logger;
import org.envtools.monitor.model.querylibrary.db.Category;
import org.envtools.monitor.model.updates.DataOperation;
import org.envtools.monitor.module.querylibrary.services.DataOperationResult;
import org.envtools.monitor.module.querylibrary.services.DataOperationService;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.*;
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


    @PersistenceContext
    EntityManager entityManager;

    @Override
    @Transactional
    public DataOperationResult create(String entity, Map<String, String> fields) throws
            IllegalAccessException, NoSuchMethodException, InvocationTargetException, ClassNotFoundException, IntrospectionException, InstantiationException, SystemException, NotSupportedException, HeuristicRollbackException, HeuristicMixedException, RollbackException {

        Map<String, Object> result = new HashMap<String, Object>();
        List<String> propertyId = new ArrayList<String>();

        /*Найдем все string, которые заканчиваются на _id*/
        List<String> list = new ArrayList(fields.keySet());


        for (Map.Entry<String, String> entry : fields.entrySet()) {
            if (entry.getKey().contains(ID)) {
                propertyId.add(entry.getKey().replace(ID, ""));

            } else {
                result.put(entry.getKey(), fields.get(entry.getKey()));
            }
        }


        //находим класс по entity
        Class entityClass = Class.forName(path + entity);
        BeanInfo bi = Introspector.getBeanInfo(entityClass);
        PropertyDescriptor[] pds = bi.getPropertyDescriptors();


        /*Находим все методы setXXX, которые содержат в названии id*/
        for (int i = 0; i < pds.length; i++) {

            if (pds[i].getWriteMethod() != null) {

                /*находим методы с id в классе entity*/
                for (int j = 0; j < propertyId.size(); j++) {

                    if (pds[i].getWriteMethod().getName().contains(firstUpperCase(propertyId.get(j)))) {
                        LOGGER.info("propertyId.get(j)= " + propertyId.get(j));
                        LOGGER.info("есть ID в методе " + pds[i].getWriteMethod() + "    " + propertyId.get(j));
                        LOGGER.info("Метод принимает " + Arrays.asList(pds[i].getWriteMethod().getParameterTypes()));
                        Class[] clazz = pds[i].getWriteMethod().getParameterTypes();

                     /*
                     * Узнали какие параметры принимает наш метод.
                     * Теперь необходимо достать связный объект
                     * */

                        for (int k = 0; k < clazz.length; k++) {
                            LOGGER.info("Тип параметра принимаемого методом " + Class.forName(clazz[k].getName()));

                            result.put(propertyId.get(j), entityManager.find(Class.forName(clazz[k].getName()),
                                    Long.valueOf(fields.get(propertyId.get(j) + ID))));

                            LOGGER.info("Object - связный объект " + result.get(propertyId.get(j)));
                        }
                    }

                }

            }

        }

        Object a = entityClass.newInstance();
        BeanUtils.populate(a, result);
        LOGGER.info("Полученный результат:  " + a);

        entityManager.persist(a);

        return DataOperationResult.builder().status(DataOperationResult.DataOperationStatusE.COMPLETED).build();
    }

    @Override
    public DataOperationResult update(String entity, Long id, Map<String, String> fields) {
        return null;
    }

    @Override
    public DataOperationResult delete(String entity, Long id) {
        return null;
    }

    public String firstUpperCase(String text) {
        if (text == null || text.isEmpty()) return "";
        return text.substring(0, 1).toUpperCase() + text.substring(1);
    }
}
