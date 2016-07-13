package org.envtools.monitor.common.ssh;

/**
 * Created by MSkuza on 2016-07-01.
 */
public interface SshHelperService {

    void register(String host, SshHelper sshHelper);

    SshHelper getHelper(String host);
}
