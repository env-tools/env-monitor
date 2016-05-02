package org.envtools.monitor.module.querylibrary.services.impl.execution;

import com.google.common.collect.Maps;
import edu.umd.cs.findbugs.annotations.Nullable;
import org.apache.log4j.Logger;
import org.envtools.monitor.module.querylibrary.services.QueryExecutionTask;
import org.envtools.monitor.module.querylibrary.services.QueryExecutionTaskRegistry;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.Future;

/**
 * Created: 01.05.16 8:33
 *
 * @author Yury Yakovlev
 */
@Service
public class QueryExecutionTaskRegistryImpl implements QueryExecutionTaskRegistry{

    private static final Logger LOGGER = Logger.getLogger(QueryExecutionTaskRegistryImpl.class);

    private ConcurrentMap<String, QueryExecutionTask> tasks = Maps.newConcurrentMap();
    private ConcurrentMap<String, Future<?>> tasksFutures = Maps.newConcurrentMap();

    @Override
    public void registerTask(String operationId, QueryExecutionTask task) {

        LOGGER.info(String.format(
                "QueryExecutionTaskRegistryImpl.registerTask - task operationId %s, task %s", operationId, task));

        tasks.put(operationId, task);
    }

    @Override
    public void registerTaskFuture(String operationId, Future<?> taskFuture) {

        LOGGER.info(String.format(
                "QueryExecutionTaskRegistryImpl.registerTaskFuture - task  %s, future %s",
                operationId, taskFuture));

        tasksFutures.put(operationId, taskFuture);
    }

    @Override
    public void remove(String operationId, QueryExecutionTask task) {

        LOGGER.info(String.format(
                "QueryExecutionTaskRegistryImpl.remove - task operationId %s, task %s", operationId, task));

        tasks.remove(operationId, task);
    }

    @Override
    @Nullable
    public QueryExecutionTask find(String id) {

        QueryExecutionTask task = tasks.get(id);

        LOGGER.info(String.format(
                "QueryExecutionTaskRegistryImpl.find - task id %s, task %s", id, task));

        return task;
    }

    @Override
    public void cancel(String operationId) {

        LOGGER.info(String.format(
                "QueryExecutionTaskRegistryImpl.cancel - task id %s", operationId));

        //First try to cancel with cancel flag
        QueryExecutionTask task = tasks.get(operationId);

        if (task != null) {
            task.cancel();
        }

        //Then try to interrupt task
        Future<?> taskFuture = tasksFutures.get(operationId);

        if (taskFuture != null) {
            try {
                taskFuture.cancel(true);
            } catch (Throwable t) {
                LOGGER.error("Exception cancelling task with id " + operationId, t);
            }
        }
    }
}
