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

    /**
     * What was the outcome of query execution
     */
    private final ExecutionStatusE status;


    /**
     * How much time it took to execute lastly
     * Say: executed 20 sec initially (1st stage, returned 50 rows) ,
     *  3 sec when user requested more rows (2nd stage, returned 24 rows).
     *  So there will be first result with elapsedTimeMs = 20000,
     *  and for the second request there will be second result with elapsedTimeMs = 3000
     */
    private final long elapsedTimeMs;

    /**
     * How many rows returned for this stage.
     * For the example above 2 results will be produced,
     * with returnedRowCount=50 for 1st stage and returnedRowCount=24 for 2nd stage
     */
    private final long returnedRowCount;

    /**
     * Result rows as provided by the ResultSet object
     */
    private final List<Map<String, Object>> resultRows;

    /**
     * Detailed error message or Optional.absent() if there was no error
     */
    private final Optional<String> errorMessage;

    /**
     * Exception occurred or Optional.absent() if there was no exception
     */
    private final Optional<Throwable> error;

    public QueryExecutionResult(ExecutionStatusE status, long elapsedTimeMs, long returnedRowCount, List<Map<String, Object>> resultRows, Optional<String> errorMessage, Optional<Throwable> error) {
        this.status = status;
        this.elapsedTimeMs = elapsedTimeMs;
        this.returnedRowCount = returnedRowCount;
        this.resultRows = resultRows;
        this.errorMessage = errorMessage;
        this.error = error;
    }
}
