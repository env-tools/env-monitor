package org.envtools.monitor.model.querylibrary.execution;

import edu.umd.cs.findbugs.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Created: 27.02.16 3:31
 *
 * @author Yury Yakovlev
 */
public class QueryExecutionResult {

    public enum ExecutionStatusE {
        COMPLETED, HAS_MORE_DATA, ERROR, TIMED_OUT
    }

    private final long elapsedTimeMs;
    private final long returnedRowCount;

    private final List<Map<String, Object>> resultRows;

    private final Optional<String> errorMessage;
    private final Optional<Throwable> error;

    public QueryExecutionResult(long elapsedTimeMs, long returnedRowCount, List<Map<String, Object>> resultRows, Optional<String> errorMessage, Optional<Throwable> error) {
        this.elapsedTimeMs = elapsedTimeMs;
        this.returnedRowCount = returnedRowCount;
        this.resultRows = resultRows;
        this.errorMessage = errorMessage;
        this.error = error;
    }
}
