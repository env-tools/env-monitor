package org.envtools.monitor.module.exception;

/**
 * Created by IAvdeev on 12.01.2017.
 */
public class BootstrapZipParseException extends RuntimeException {
    public BootstrapZipParseException() {
        super();
    }

    public BootstrapZipParseException(String message) {
        super(message);
    }

    public BootstrapZipParseException(String message, Throwable cause) {
        super(message, cause);
    }

    public BootstrapZipParseException(Throwable cause) {
        super(cause);
    }

    protected BootstrapZipParseException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
