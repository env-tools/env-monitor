package org.envtools.monitor.module.querylibrary.dao;

import org.envtools.monitor.model.querylibrary.db.QueryExecutionParam;

import java.util.List;

/**
 * Created: 10.03.16 22:37
 *
 * @author Anastasiya Plotnikova
 */
public interface QueryExecutionParamDao extends Dao<QueryExecutionParam, Long> {

    List<QueryExecutionParam> getNameByText(String text);

    List<QueryExecutionParam> getValueByText(String text);

}
