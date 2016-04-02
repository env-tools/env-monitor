package org.envtools.monitor.common.serialization.aggregator;


import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

/**
 * Created: 02.04.2016
 *
 * @author sergey
 */

@Service
public class JsonAggregator implements Aggregator {
    public String aggregate(String... parts) {
        String joinedString = StringUtils.join(parts, ',');

        return "[" + joinedString + "]";
    }
}
