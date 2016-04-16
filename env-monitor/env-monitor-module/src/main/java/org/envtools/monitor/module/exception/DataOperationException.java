package org.envtools.monitor.module.exception;

/**
 * Created: 16.04.16 23:36
 *
 * @author Anastasiya Plotnikova
 */
public class  DataOperationException extends Exception {

    public DataOperationException() {
    }

    public DataOperationException(String message) {
        super(message);
    }

    public DataOperationException(String message, Throwable cause) {
        super(message, cause);
    }

    public DataOperationException(Throwable cause) {
        super(cause);
    }

    public DataOperationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
