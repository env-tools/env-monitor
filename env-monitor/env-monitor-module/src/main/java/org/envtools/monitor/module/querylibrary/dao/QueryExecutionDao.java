package org.envtools.monitor.module.querylibrary.dao;

import org.envtools.monitor.model.querylibrary.db.QueryExecution;

import java.time.LocalTime;
import java.util.List;

/**
 * Created: 10.03.16 21:54
 *
 * @author Anastasiya Plotnikova
 */
public interface QueryExecutionDao extends Dao<QueryExecution, Long> {
    List<QueryExecution> getUserByText(String text);
    List<QueryExecution> getstartTimestamp(LocalTime time);
    List<QueryExecution> getEndTimestamp(LocalTime time);
}
