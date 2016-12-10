package org.envtools.monitor.common.ssh;

import org.apache.commons.lang.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.Arrays;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

/**
 * Created: 12/6/16 9:09 PM
 *
 * @author Yury Yakovlev
 */
public class SshBatchTest {

    private SshBatch sshBatch;
    private SshHelperService sshHelperService;

    private SshHelper sshHelper;

    private static final String TEST_HOST = "test.com";

    @Before
    public void setUp() throws Exception {
        sshHelper = mock(SshHelper.class);

        //Just return the command itself as the result
        when(sshHelper.cmd(anyString())).thenAnswer(invocationOnMock -> {

            //Here we emulate relevant 'command execution'
            String cmd = String.valueOf(invocationOnMock.getArguments()[0]);
            String[] cmds = cmd.split(";");
            StringBuilder result = new StringBuilder();
            for (String singleCmd : cmds) {
                if (singleCmd.startsWith("echo ")) {
                    result.append(singleCmd.substring("echo ".length()));
                } else if ("NO_OP".equals(singleCmd)) {
                    //do not append anything
                } else {
                    result.append(singleCmd);
                }
                result.append("\n");

            }

            return result.toString();
        });

        sshHelperService = mock(SshHelperService.class);
        when(sshHelperService.getHelper(anyString())).thenReturn(sshHelper);

        sshBatch = new SshBatch(sshHelperService);

    }

    @Test
    public void testSingleCommandBatch() {
        Consumer<String> mockHandler = mock(Consumer.class);

        sshBatch.addToBatch(TEST_HOST, "cmd1", mockHandler);
        sshBatch.execute();

        verify(mockHandler, times(1)).accept("cmd1");

    }

    @Test
    public void testTwoCommandBatch() {
        Consumer<String> mockHandler1 = mock(Consumer.class);
        Consumer<String> mockHandler2 = mock(Consumer.class);

        sshBatch.addToBatch(TEST_HOST, "cmd1", mockHandler1);
        sshBatch.addToBatch(TEST_HOST, "cmd2", mockHandler2);

        sshBatch.execute();

        verify(mockHandler1, times(1)).accept("cmd1");
        verify(mockHandler2, times(1)).accept("cmd2");

    }

    @Test
    public void testCommandsWithEmptyResults() {
        Consumer<String> mockHandler1 = mock(Consumer.class);
        Consumer<String> mockHandler2 = mock(Consumer.class);

        sshBatch.addToBatch(TEST_HOST, "cmd1", mockHandler1);
        sshBatch.addToBatch(TEST_HOST, "NO_OP", mockHandler2);

        sshBatch.execute();

        verify(mockHandler1, times(1)).accept("cmd1");
        verify(mockHandler2, times(1)).accept("");

    }
}
