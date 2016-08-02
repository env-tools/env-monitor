package org.envtools.monitor.common.ssh;

import com.jcraft.jsch.JSchException;
import org.envtools.monitor.common.encrypting.EncryptionService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Michal Skuza on 2016-07-13.
 */
public class SshHelperServiceFactory {

    private final int SSH_PORT = 22;
    private final SshCredentialsService sshCredentialsService;
    private final EncryptionService encryptionService;

    public SshHelperServiceFactory(SshCredentialsService sshCredentialsService, EncryptionService encryptionService) {
        this.sshCredentialsService = sshCredentialsService;
        this.encryptionService = encryptionService;
    }

    public SshHelperService buildSshHelperService(List<String> hosts) {
        SshHelperService sshHelperService = new SshHelperServiceImpl();

        for (Map.Entry<String, Credentials> entry : getCredentialToLoadAtStartup(hosts).entrySet()) {
            Credentials credentials = entry.getValue();

            try {
                SshHelper sshHelper = createSshHelper(credentials);
                sshHelperService.register(entry.getKey(), sshHelper);
            } catch (JSchException e) {
                throw new RuntimeException(e);
            }
        }

        return sshHelperService;
    }

    SshHelper createSshHelper(Credentials credentials) throws JSchException {
        SshHelper sshHelper = new SshHelper(credentials.getUser(), credentials.getHost(), SSH_PORT);
        sshHelper.setPassword(decryptPassword(credentials));
        return sshHelper;
    }

    private Map<String, Credentials> getCredentialToLoadAtStartup(List<String> hosts) {
        Map<String, Credentials> credentialsMap = new HashMap<>();

        for (String host : hosts) {
            Credentials credentials = sshCredentialsService.getCredentials(host);

            if (credentials.isLoadAtStartup())
                credentialsMap.put(credentials.getHost(), credentials);
        }

        return credentialsMap;
    }

    private String decryptPassword(Credentials credentials) {
        return encryptionService.decrypt(credentials.getEncryptedPassword());
    }
}
