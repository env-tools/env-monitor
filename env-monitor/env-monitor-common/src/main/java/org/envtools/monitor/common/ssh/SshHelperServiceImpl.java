package org.envtools.monitor.common.ssh;

import com.jcraft.jsch.JSchException;

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

    @Override
    public void loginAllSshHelpers() {
        for (SshHelper helper : sshHelpersMap.values()) {
            try {
                helper.login();
            } catch (JSchException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void logoutAllSshHelpers() {
        for (SshHelper helper : sshHelpersMap.values()) {
            helper.logout();
        }
    }
}
