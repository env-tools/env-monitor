package org.envtools.monitor.provider.applications.remote;

import java.util.Map;
import java.util.Optional;

/**
 * Created: 22.12.16 0:15
 *
 * @author Yury Yakovlev
 */
public interface BasicUrlService {

    Optional<String> readResource(String url, Map<String, String> headers);

}
