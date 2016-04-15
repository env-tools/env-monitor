package org.envtools.monitor.model.querylibrary.execution;

import edu.umd.cs.findbugs.annotations.Nullable;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.envtools.monitor.common.util.ExceptionReportingUtil;

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

    private final String executionId;

    /**
     * What was the outcome of query execution
     */
    private final ExecutionStatusE status;


    /**
     * How much time it took to execute lastly
     * Say: executed 20 sec initially (1st stage, returned 50 rows) ,
     * 3 sec when user requested more rows (2nd stage, returned 24 rows).
     * So there will be first result with elapsedTimeMs = 20000,
     * and for the second request there will be second result with elapsedTimeMs = 3000
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

    public QueryExecutionResult(String executionId, ExecutionStatusE status, long elapsedTimeMs, long returnedRowCount, List<Map<String, Object>> resultRows, Optional<String> errorMessage, Optional<Throwable> error) {
        this.executionId = executionId;
        this.status = status;
        this.elapsedTimeMs = elapsedTimeMs;
        this.returnedRowCount = returnedRowCount;
        this.resultRows = resultRows;
        this.errorMessage = errorMessage;
        this.error = error;
    }

    public String getExecutionId() {
        return executionId;
    }

    public ExecutionStatusE getStatus() {
        return status;
    }

    public long getElapsedTimeMs() {
        return elapsedTimeMs;
    }

    public long getReturnedRowCount() {
        return returnedRowCount;
    }

    public List<Map<String, Object>> getResultRows() {
        return resultRows;
    }

    public Optional<String> getErrorMessage() {
        return errorMessage;
    }

    public Optional<Throwable> getError() {
        return error;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String executionId;
        private ExecutionStatusE status;
        private long elapsedTimeMs;
        private long returnedRowCount;
        private List<Map<String, Object>> resultRows;
        private String errorMessage;
        private Throwable error;

        public Builder executionId(String executionId) {
            this.executionId = executionId;
            return this;
        }

        public Builder status(ExecutionStatusE status) {
            this.status = status;
            return this;
        }

        public Builder elapsedTimeMs(long elapsedTimeMs) {
            this.elapsedTimeMs = elapsedTimeMs;
            return this;
        }

        public Builder returnedRowCount(long returnedRowCount) {
            this.returnedRowCount = returnedRowCount;
            return this;
        }

        public Builder resultRows(List<Map<String, Object>> resultRows) {
            this.resultRows = resultRows;
            return this;
        }

        public Builder errorMessage(String errorMessage) {
            this.errorMessage = errorMessage;
            return this;
        }

        public Builder error(Throwable error) {
            this.error = error;
            return this;
        }

        public QueryExecutionResult build() {
            return new QueryExecutionResult(
                    executionId,
                    status,
                    elapsedTimeMs,
                    returnedRowCount,
                    resultRows,
                    errorMessage == null ? Optional.<String>empty() : Optional.of(errorMessage),
                    error == null ? Optional.<Throwable>empty() : Optional.of(error));
        }


    }

    public static QueryExecutionResult ofError(String executionId, Throwable t) {
        return QueryExecutionResult
                .builder()
                .executionId(executionId)
                .status(ExecutionStatusE.ERROR)
                .errorMessage(ExceptionReportingUtil.getExceptionMessage(t))
                .error(t)
                .build();
    }
}
