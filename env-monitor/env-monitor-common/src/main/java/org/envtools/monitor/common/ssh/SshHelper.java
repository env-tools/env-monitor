package org.envtools.monitor.common.ssh;

import com.jcraft.jsch.*;
import org.apache.log4j.Logger;

import java.io.InputStream;

/**
 * Created by Michal Skuza on 09/06/16.
 */
public class SshHelper {
    private static final Logger LOGGER = Logger.getLogger(SshHelper.class);

    private final String user;
    private final String host;
    private final int port;
    private UserInfo userInfo;

    private Session session;

    public SshHelper(String user, String host, int port) {
        this.user = user;
        this.host = host;
        this.port = port;
    }

    private MyUserInfo createUserInfo(final String password) {
        return new MyUserInfo(password);
    }

    public void setPassword(final String password) {
        userInfo = createUserInfo(password);
    }

    public void login() throws JSchException {
        JSch jsch = new JSch();
        session = jsch.getSession(user, host, port);
        session.setUserInfo(userInfo);
        session.connect();
        LOGGER.info(String.format("logged in as %s@%s",  user, host));

    }

    public void logout() {
        session.disconnect();
        LOGGER.info(String.format("%s@%s logged out",  user, host));
    }

    public String cmd(String command) throws JSchException {
        LOGGER.info("exec : " + command);
        InputStream in;
        StringBuilder str = new StringBuilder();
        Channel channel = session.openChannel("exec");

        try {

            ((ChannelExec) channel).setCommand(command);
            channel.setInputStream(null);
            ((ChannelExec) channel).setErrStream(System.err);

            in = channel.getInputStream();

            channel.connect();

            byte[] tmp = new byte[1024];
            while (true) {
                while (in.available() > 0) {
                    int i = in.read(tmp, 0, 1024);
                    if (i < 0) break;
                    String out = new String(tmp, 0, i);
                    str.append(out);
                }

                if (channel.isClosed()) {
                    LOGGER.info("exit-status: " + channel.getExitStatus());
                    break;
                }
                try {
                    Thread.sleep(1000);
                } catch (Exception ee) {
                }
            }

        } catch (Exception e) {
            throw new JSchException(e.getMessage(), e);
        } finally {
            channel.disconnect();
        }
        return str.toString();
    }

    private static class MyUserInfo implements UserInfo, UIKeyboardInteractive {
        private final String password;

        public MyUserInfo(String password) {
            this.password = password;
        }

        @Override
        public String getPassphrase() {
            return null;
        }

        @Override
        public String getPassword() {
            return password;
        }

        @Override
        public boolean promptPassword(String s) {
            return true;
        }

        @Override
        public boolean promptPassphrase(String s) {
            return true;
        }

        @Override
        public boolean promptYesNo(String s) {
            return true;
        }

        @Override
        public void showMessage(String s) {
        }

        @Override
        public String[] promptKeyboardInteractive(String destination, String name, String instruction, String[] prompt, boolean[] echo) {
            if ("PAM Authentication".equals(instruction) && prompt.length > 0 && "Password: ".equals(prompt[0]))
                return new String[]{password};
            else
                return new String[0];
        }
    }
}