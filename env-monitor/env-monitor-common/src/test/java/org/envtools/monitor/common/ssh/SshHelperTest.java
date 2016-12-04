package org.envtools.monitor.common.ssh;

import org.apache.commons.lang.time.StopWatch;
import org.junit.*;

public class SshHelperTest {
    SshHelper sshHelper;
    private final int port = 22;
    private final String host = "";
    private final String user = "";
    private final String password = "";
    private final long sshInterval = 1000;

    @Before
    public void setUp() throws Exception {
        sshHelper = new SshHelper(user, host, port, sshInterval);
        sshHelper.setPassword(password);
        sshHelper.login();
    }

    @Ignore
    @Test
    public void testIsLoggedInAsUser() throws Exception {
        StopWatch stopwatch = new StopWatch();
        stopwatch.start();

        String command = "whoami";
        String response = sshHelper.cmd(command);

        stopwatch.stop();
        System.out.println("That took: " + stopwatch.getTime() + " ms");

        Assert.assertTrue(user.equals(response.trim()));
    }

    @Ignore
    @Test
    public void testCanExecuteCommands() throws Exception {
        StopWatch stopwatch = new StopWatch();
        stopwatch.start();

        String command = "ps -ef";
        String response = sshHelper.cmd(command);

        stopwatch.stop();
        System.out.println("That took: " + stopwatch.getTime() + " ms");

        Assert.assertFalse(response.trim().isEmpty());
    }

    @After
    public void tearDown() throws Exception {
        sshHelper.logout();
    }
}