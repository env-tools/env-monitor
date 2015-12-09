package org.envtools.monitor.module.core.selection.exception;

/**
 * Created: 12/5/15 2:16 AM
 *
 * @author Yury Yakovlev
 */
public class IllegalSelectorException extends Exception{
    public IllegalSelectorException() {
    }

    public IllegalSelectorException(String message) {
        super(message);
    }

    public IllegalSelectorException(String message, Throwable cause) {
        super(message, cause);
    }

    public IllegalSelectorException(Throwable cause) {
        super(cause);
    }

    public IllegalSelectorException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
