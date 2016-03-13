package org.envtools.monitor.module.querylibrary.dao;

import org.envtools.monitor.model.querylibrary.db.LibQuery;
import org.envtools.monitor.model.querylibrary.db.QueryParam;

import java.util.List;

/**
 * Created: 10.03.16 21:25
 *
 * @author Anastasiya Plotnikova
 */
public interface QueryParamDao extends Dao<QueryParam, Long> {
    List<QueryParam> getNameQueryParamByText(String text);
}
