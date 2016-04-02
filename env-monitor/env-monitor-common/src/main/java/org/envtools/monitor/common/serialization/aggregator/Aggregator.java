package org.envtools.monitor.common.serialization.aggregator;

/**
 * Created: 02.04.2016
 *
 * @author sergey
 */
public interface Aggregator {
    String aggregate(String... parts);
}
