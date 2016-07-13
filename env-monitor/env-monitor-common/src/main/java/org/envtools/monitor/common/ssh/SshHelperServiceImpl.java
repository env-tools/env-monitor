package org.envtools.monitor.common.ssh;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by MSkuza on 2016-07-01.
 */
public class SshHelperServiceImpl implements SshHelperService {
    private Map<String, SshHelper> sshHelpersMap;

    public SshHelperServiceImpl() {
        this.sshHelpersMap = new HashMap<>();
    }

    @Override
    public SshHelper getHelper(String host) {
        return this.sshHelpersMap.get(host);
    }

    @Override
    public void register(String host, SshHelper sshHelper) {
        sshHelpersMap.put(host, sshHelper);
    }
}
