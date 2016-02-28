package org.envtools.monitor.model.querylibrary.execution;

/**
 * Created: 27.02.16 3:32
 *
 * @author Yury Yakovlev
 */
public class QueryExecutionException extends Exception {
    public QueryExecutionException() {
    }

    public QueryExecutionException(String message) {
        super(message);
    }

    public QueryExecutionException(String message, Throwable cause) {
        super(message, cause);
    }

    public QueryExecutionException(Throwable cause) {
        super(cause);
    }

    public QueryExecutionException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
