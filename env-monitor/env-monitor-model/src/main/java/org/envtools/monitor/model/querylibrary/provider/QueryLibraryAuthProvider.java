package org.envtools.monitor.model.querylibrary.provider;

/**
 * Created: 17.03.16 1:35
 *
 * @author Yury Yakovlev
 */
public interface QueryLibraryAuthProvider {

    String login(String user, String password);

}
