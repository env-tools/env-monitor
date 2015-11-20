package org.envtools.monitor.model.applications;

import org.envtools.monitor.model.applications.update.NotificationHandler;

/**
 * Created: 10/31/15 1:38 AM
 *
 * @author Yury Yakovlev
 */
public interface ApplicationsModuleProvider {

    void initialize(NotificationHandler handler);

    ApplicationsData getApplicationsData();

}
