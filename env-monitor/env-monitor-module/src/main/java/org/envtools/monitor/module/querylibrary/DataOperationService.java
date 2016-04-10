package org.envtools.monitor.module.querylibrary;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.Logger;
import org.envtools.monitor.model.querylibrary.execution.QueryExecutionResult;
import org.envtools.monitor.model.updates.DataOperation;
import org.envtools.monitor.module.DataOperationInterface;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

/**
 * Created: 10.04.16 12:18
 *
 * @author Anastasiya Plotnikova
 */
@Service
public class DataOperationService extends DataOperation implements DataOperationInterface {

    private static final Logger LOGGER = Logger.getLogger(DataOperationService.class);
    private static final String ID = "_id";
    private static final String path = "org.envtools.monitor.model.querylibrary.db.";

    @Override
    public QueryExecutionResult create(String entity) throws ClassNotFoundException, InvocationTargetException, IllegalAccessException, NoSuchMethodException, InstantiationException {
        entity = entity.replace("(", "").replace(")", "");
        //разбиваем полученную строку по запятым
        String[] str = entity.split(",");

        //property - хранит свойства и значения
        String[] property = null;

        //propertyID - хранит свойства и значения c ID
        Map<String, String> propertyID = null;

        //propertyNotID - хранит остальные свойства и значения
        Map<String, String> propertyNotID = null;


        //Object bean = str[0].replaceAll("\"").getClass();
        /* Найдем все поля, имя которых заканчивается на "_id" */
        for (int i = 1; i < str.length; i++) {
            property = str[i].split(":");
            if (property[0].endsWith(ID)) {
                //propertyID.put(property[0].replace(ID, ""), property[1]);
                // LOGGER.info("Свойства с _id  " + propertyID.get(property[0].replace(ID, "")));
            } else {
                // propertyNotID.put(property[0], property[0]);
                //LOGGER.info("Класс в котором содержится свойство:  " + property[0].getClass());

            }

        }
        Class c = Class.forName(path + str[0].replaceAll("\"", "").trim());
        //   BeanUtils.populate(str[0].replaceAll("\"","").trim(),propertyID);
        LOGGER.info("Полученный класс:  " + c.getName());
        return null;
    }

    @Override
    public void update(Long id, String entity) {

    }

    @Override
    public void delete(Long id, String entity) {

    }
}
