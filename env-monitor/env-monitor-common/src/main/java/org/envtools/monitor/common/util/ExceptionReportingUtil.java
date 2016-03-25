package org.envtools.monitor.common.util;

import org.apache.commons.lang.exception.ExceptionUtils;

/**
 * Created: 3/25/16 3:07 AM
 *
 * @author Yury Yakovlev
 */
public final class ExceptionReportingUtil {

    private ExceptionReportingUtil() {
    }

    public static String getExceptionMessage(Throwable t) {
        return ExceptionUtils.getRootCauseMessage(t);
    }
}
