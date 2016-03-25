package org.envtools.monitor.module.exception;

/**
 * Created: 3/25/16 12:09 AM
 *
 * @author Yury Yakovlev
 */
public class MessageFormatException extends RuntimeException{
    public MessageFormatException() {
    }

    public MessageFormatException(String message) {
        super(message);
    }

    public MessageFormatException(String message, Throwable cause) {
        super(message, cause);
    }

    public MessageFormatException(Throwable cause) {
        super(cause);
    }

    public MessageFormatException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
