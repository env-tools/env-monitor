package org.envtools.monitor.module.querylibrary.services.impl.updates;

import com.sun.javafx.css.converters.EnumConverter;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.ConvertUtilsBean;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import org.envtools.monitor.model.querylibrary.DataProviderType;
import org.envtools.monitor.model.querylibrary.updates.DataOperation;
import org.envtools.monitor.module.core.selection.exception.IllegalSelectorException;
import org.envtools.monitor.module.exception.DataOperationException;
import org.envtools.monitor.module.querylibrary.services.DataOperationResult;
import org.envtools.monitor.module.querylibrary.services.DataOperationService;
import org.hibernate.HibernateException;
import org.hibernate.exception.ConstraintViolationException;

import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.*;
import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Created: 10.04.16 12:18
 *
 * @author Anastasiya Plotnikova
 */
@Service
public class DataOperationServiceImpl implements DataOperationService<Long> {

    private static final Logger LOGGER = Logger.getLogger(DataOperationServiceImpl.class);
    private static final String ID_FLAG = "_id";
    private static final String path = "org.envtools.monitor.model.querylibrary.db.";

    @PersistenceContext
    EntityManager entityManager;


    @Override
    @Transactional
    public DataOperationResult create(String entity, Map<String, String> fields) throws IntrospectionException, IllegalAccessException, InstantiationException, InvocationTargetException, ClassNotFoundException, DataOperationException,HibernateException {
        try {
            Class entityClass = Class.forName(path + entity);
            Map<String, Object> propertyValues = resolveIdPropertyValues(entityClass, fields);
            Object createdEntity = entityClass.newInstance();
            BeanUtils.populate(createdEntity, propertyValues);
            LOGGER.info("Полученный результат:  " + createdEntity);
            entityManager.persist(createdEntity);
            return DataOperationResult.builder().status(DataOperationResult.DataOperationStatusE.COMPLETED).build();
        } catch (Throwable t)
        {
            LOGGER.error(t.getMessage(), t);
            return DataOperationResult.builder().status(DataOperationResult.DataOperationStatusE.ERROR).errorMessage(t.getMessage()).error(t).build();
        }


    }

    @Override
    public DataOperationResult update(String entity, Long id, Map<String, String> fields) throws ClassNotFoundException, IllegalArgumentException,HibernateException {
        /*Вытаскиваем класс, делаем find, по id, втавляем туда fields*/
        //находим класс по entity
        try {
            Class entityClass = Class.forName(path + entity);
            Object object = entityManager.find(entityClass, id);
            Map<String, Object> propertyValues = resolveIdPropertyValues(entityClass, fields);
            BeanUtils.populate(object, propertyValues);
            LOGGER.info("Полученный результат:  " + object);
            entityManager.persist(object);
            return DataOperationResult.builder().status(DataOperationResult.DataOperationStatusE.COMPLETED).build();
        } catch (ClassNotFoundException | IllegalArgumentException | IllegalAccessException |
                InvocationTargetException | DataOperationException|HibernateException e) {
            LOGGER.info(e.getMessage());
            return DataOperationResult.builder().status(DataOperationResult.DataOperationStatusE.ERROR).errorMessage(e.getMessage()).error(e).build();
        }

    }

    @Override
    public DataOperationResult delete(String entity, Long id) throws ClassNotFoundException, HibernateException, IllegalArgumentException, ConstraintViolationException {
        Class entityClass = Class.forName(path + entity);
        Object object = entityManager.find(entityClass, id);
        entityManager.remove(object);
        return DataOperationResult.builder().status(DataOperationResult.DataOperationStatusE.COMPLETED).build();
    }


    public Map<String, Object> resolveIdPropertyValues(Class entityClass, Map<String, String> fields) throws NumberFormatException, DataOperationException {

        Map<String, Object> propertyValues = new HashMap<>();
        List<String> propertyId = new ArrayList<>();


        try {
            BeanInfo bi = Introspector.getBeanInfo(entityClass);
            PropertyDescriptor[] pds = bi.getPropertyDescriptors();

            /*Найдем все string, которые заканчиваются на _id*/
            for (Map.Entry<String, String> entry : fields.entrySet()) {
                if (entry.getKey().endsWith(ID_FLAG)) {
                    propertyId.add(entry.getKey().replace(ID_FLAG, ""));

                } else {
                    propertyValues.put(entry.getKey(), entry.getValue());
                }
            }


            for (int i = 0; i < pds.length; i++) {

                if (pds[i].getWriteMethod() != null) {
                    for (Map.Entry<String, Object> entryParam : propertyValues.entrySet()) {

                        if (pds[i].getName().contains(entryParam.getKey())) {
                            for (Class<?> entry : pds[i].getWriteMethod().getParameterTypes()) {
                                if (entry != String.class) {
                                    if (entry.isEnum()) {
                                        LOGGER.info("ddddddddd" + entry);
                                         //propertyValues.put( entryParam.getKey(), ConvertUtils.convert(entryParam.getValue(),(Class)entry))  ;
                                        propertyValues.put(entryParam.getKey(),
                                                Enum.valueOf((Class) entry, (String) entryParam.getValue()));

                                    } else if (entry.getName().equals(LocalDateTime.class.getName())) {
                                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                                        propertyValues.put(entryParam.getKey(), LocalDateTime.parse((String) entryParam.getValue(), formatter));
                                    }

                                }
                            }
                        }
                    }

                }

            }

            /*Находим все методы setXXX, которые содержат в названии id*/
            for (int i = 0; i < pds.length; i++) {

                if (pds[i].getWriteMethod() != null) {

                /*находим методы с id в классе entity*/
                    for (String entry : propertyId) {
                        if (pds[i].getName().contains(entry)) {
                            LOGGER.info("propertyId.get(j)= " + entry);
                            LOGGER.info("есть ID в методе " + pds[i].getWriteMethod() + "    " + entry);
                            LOGGER.info("Метод принимает " + Arrays.asList(pds[i].getWriteMethod().getParameterTypes()));
                            Class[] setterParameterTypes = pds[i].getWriteMethod().getParameterTypes();

                            if (setterParameterTypes.length != 1) throw new IllegalSelectorException(
                                    new IllegalSelectorException(String.format("Invalid number of arguments %s, " +
                                            "expected 1", setterParameterTypes.length)));

                            LOGGER.info("Тип параметра принимаемого методом " + Class.forName(setterParameterTypes[0].getName()));

                            propertyValues.put(entry, entityManager.find(Class.forName(setterParameterTypes[0].getName()),
                                    Long.valueOf(fields.get(entry + ID_FLAG))));

                            LOGGER.info("Object - связный объект " + propertyValues.get(entry));

                        }
                    }

                }

            }

        } catch (ClassNotFoundException | NumberFormatException | IntrospectionException | IllegalSelectorException e) {

            throw new DataOperationException("Create operation falied", e);
        }
        return propertyValues;
    }


    public String firstUpperCase(String text) {
        if (StringUtils.isEmpty(text)) return "";
        return text.substring(0, 1).toUpperCase() + text.substring(1);
    }
}
