package org.envtools.monitor.module.querylibrary.services;

import edu.umd.cs.findbugs.annotations.Nullable;

import java.util.concurrent.Future;

/**
 * Created: 01.05.16 8:30
 *
 * @author Yury Yakovlev
 */
public interface QueryExecutionTaskRegistry {

    void registerTask(String operationId, QueryExecutionTask task);

    void registerTaskFuture(String operationId, Future<?> taskFuture);

    void remove(String operationId, QueryExecutionTask task);

    @Nullable
    QueryExecutionTask find(String id);

    void cancel(String operationId);

}
