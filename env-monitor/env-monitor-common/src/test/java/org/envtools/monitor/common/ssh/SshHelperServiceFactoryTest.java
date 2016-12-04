package org.envtools.monitor.common.ssh;

import org.envtools.monitor.common.encrypting.EncryptionService;
import org.envtools.monitor.common.encrypting.EncryptionServiceImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.*;

/**
 * Created by Michal Skuza on 13/07/16.
 */
public class SshHelperServiceFactoryTest {
    private SshHelperServiceFactory sshHelperServiceFactory;
    private List<String> hostsList;
    private List<String> activeHosts;

    @Before
    public void setUp() throws Exception {
        activeHosts = Arrays.asList("ULTRON.net", "VYVERN.net");

        Map<String, Credentials> map = new HashMap<>();
        map.put("ULTRON.net", new Credentials("User1", "ULTRON.net", "password", "drowssap", true));
        map.put("GUNDYR.net", new Credentials("User2", "GUNDYR.net", "password2", "2drowssap", false));
        map.put("VYVERN.net", new Credentials("User3", "VYVERN.net", "password3", "3drowssap", true));

        SshCredentialsService sshCredentialsService = Mockito.mock(SshCredentialsServiceImpl.class);

        for (Map.Entry<String, Credentials> entry : map.entrySet()) {
            Mockito.doReturn(entry.getValue()).when(sshCredentialsService).getCredentials(entry.getKey());
        }

        hostsList = new ArrayList<>(map.keySet());

        EncryptionService encryptionService = Mockito.mock(EncryptionServiceImpl.class);
        Mockito.doReturn("password").when(encryptionService).decrypt(Mockito.anyString());

        sshHelperServiceFactory = Mockito.spy(new SshHelperServiceFactory(sshCredentialsService, encryptionService, 1000));
        Mockito.doReturn(new SshHelper("some_host", "user", 22, 1000)).when(sshHelperServiceFactory).createSshHelper(Mockito.any(Credentials.class));
    }

    @Test
    public void SshHelperServiceFactoryCanCreateSshHelpers() {
        SshHelperService sshHelperService = sshHelperServiceFactory.buildSshHelperService(hostsList);

        for (String host : activeHosts) {
            Assert.assertNotNull(sshHelperService.getHelper(host));
        }
    }
}