package org.envtools.monitor.module.querylibrary.dao;

import org.envtools.monitor.model.querylibrary.db.QueryExecution;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

/**
 * Created: 10.03.16 21:54
 *
 * @author Anastasiya Plotnikova
 */
public interface QueryExecutionDao extends Dao<QueryExecution, Long> {

    List<QueryExecution> getByTextInUserName(String text);

    List<QueryExecution> getByStartTimeInterval(LocalDateTime startTime, LocalDateTime endTime);

}
