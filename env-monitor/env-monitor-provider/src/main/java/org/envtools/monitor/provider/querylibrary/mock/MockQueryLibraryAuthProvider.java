package org.envtools.monitor.provider.querylibrary.mock;

import org.envtools.monitor.model.querylibrary.provider.QueryLibraryAuthProvider;

import java.util.UUID;

/**
 * Created: 17.03.16 1:37
 *
 * @author Yury Yakovlev
 */
public class MockQueryLibraryAuthProvider implements QueryLibraryAuthProvider {
    @Override
    public String login(String user, String password) {
        return UUID.randomUUID().toString();
        //TODO check login/password and generate and return token
    }
}
