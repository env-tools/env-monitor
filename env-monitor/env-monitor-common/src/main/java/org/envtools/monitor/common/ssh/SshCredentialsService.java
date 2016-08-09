package org.envtools.monitor.common.ssh;

/**
 * Created by Michal Skuza on 2016-06-17.
 */
public interface SshCredentialsService {

    Credentials getCredentials(String host);

}
