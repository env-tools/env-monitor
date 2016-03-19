package org.envtools.monitor.module.core.cache;

/**
 * Created: 3/19/16 3:52 AM
 *
 * @author Yury Yakovlev
 */
public interface DataPushService {

    void pushToSubscribedClient(String destination, String content);

}
