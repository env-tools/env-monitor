package org.envtools.monitor.module.querylibrary.services;

import org.envtools.monitor.common.util.ExceptionReportingUtil;
import org.envtools.monitor.model.querylibrary.execution.QueryExecutionResult;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Created: 11.04.16 16:00
 *
 * @author Anastasiya Plotnikova
 */
public class DataOperationResult {


    public enum DataOperationStatusE {
        COMPLETED, ERROR
    }

    private final String executionId;

    /**
     * What was the outcome of query execution
     */
    private final DataOperationStatusE status;

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

    public DataOperationResult(String executionId, DataOperationStatusE status, List<Map<String, Object>> resultRows, Optional<String> errorMessage, Optional<Throwable> error) {
        this.executionId = executionId;
        this.status = status;
        this.resultRows = resultRows;
        this.errorMessage = errorMessage;
        this.error = error;
    }

    public String getExecutionId() {
        return executionId;
    }

    public DataOperationStatusE getStatus() {
        return status;
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
        private  String executionId;
        private  DataOperationStatusE status;
        private  long elapsedTimeMs;
        private  long returnedRowCount;
        private  List<Map<String, Object>> resultRows;
        private  String errorMessage;
        private  Throwable error;

        public Builder executionId(String executionId) {
            this.executionId = executionId;
            return this;
        }

        public Builder status(DataOperationStatusE status) {
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

        public DataOperationResult build() {
            return new DataOperationResult(
                    executionId,
                    status,
                    resultRows,
                    errorMessage == null ? Optional.<String>empty() : Optional.of(errorMessage),
                    error == null ? Optional.<Throwable>empty() : Optional.of(error));
        }
    }

    public static DataOperationResult ofError(String executionId, Throwable t) {
        return DataOperationResult
                .builder()
                .executionId(executionId)
                .status(DataOperationStatusE.ERROR)
                .errorMessage(ExceptionReportingUtil.getExceptionMessage(t))
                .error(t)
                .build();
    }
}
