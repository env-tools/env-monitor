package org.envtools.monitor.module.querylibrary.services;

import java.util.Map;

/**
 * Created: 13.03.16 1:37
 *
 * @author Yury Yakovlev
 */
public interface DataSourceService <T> {

    T getDataSourceForParams(Map<String, String> params);

}
