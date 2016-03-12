package org.envtools.monitor.module.querylibrary.services.impl.datasource;

import com.google.common.collect.Maps;
import com.google.common.util.concurrent.Striped;
import org.apache.log4j.Logger;
import org.envtools.monitor.module.querylibrary.services.DataSourceService;

import javax.annotation.PreDestroy;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.concurrent.locks.Lock;
import java.util.stream.Collectors;

/**
 * Created: 13.03.16 2:01
 *
 * @author Yury Yakovlev
 */
public abstract class AbstractDataSourceService<T> implements DataSourceService<T> {

    private static final Logger LOGGER = Logger.getLogger(AbstractDataSourceService.class);

    private static final int N_PRECREATED_LOCKS = 20;

    private Striped<Lock> locks = Striped.lock(N_PRECREATED_LOCKS);

    private Map<String, T> dataSources = Maps.newConcurrentMap();

    @Override
    public T getDataSourceForParams(Map<String, String> params) {
        String key = createKeyString(params);
        if (!dataSources.containsKey(key)) {
            //http://stackoverflow.com/questions/11124539/how-to-acquire-a-lock-by-a-key
            Lock lockByKey = locks.get(key);
            lockByKey.lock();
            try {
                //Perform the double check
                if (!dataSources.containsKey(key)) {
                     T dataSource = createDataSource(params);
                    LOGGER.info("AbstractDataSourceService.getDataSourceForParams - " + " created data source for " + key);
                    dataSources.put(key, dataSource);
                    return dataSource;
                }
            } finally {
                lockByKey.unlock();
            }
        }
        LOGGER.info("AbstractDataSourceService.getDataSourceForParams - " +
                " existed data source for " + key);
        return dataSources.get(key);
    }

    //Creates single concatenated key from map of parameters
    private String createKeyString(Map<String, String> params) {
        //There is a Joiner.MapJoiner in guava, but we prefer Java 8 way
        SortedMap<String, String> sortedMap = new TreeMap<>(getKeyParams(params));
        return sortedMap
                .entrySet()
                .stream()
                .map(e -> e.getKey() + "=" + e.getValue())
                .collect(Collectors.joining(";"));
    }

    protected abstract Map<String, String> getKeyParams(Map<String, String> allParams);

    protected abstract T createDataSource(Map<String, String> params);

    protected abstract void close(T dataSource);

    @PreDestroy
    public void close() {
        for (Map.Entry<String, T> dataSourceEntry : dataSources.entrySet()) {
            LOGGER.info("AbstractDataSourceService.close - closing DS " +
                    dataSourceEntry.getKey());
            close(dataSourceEntry.getValue());
        }
    }
}
