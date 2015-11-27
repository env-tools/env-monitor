package org.envtools.monitor.model.applications;

import org.envtools.monitor.model.applications.update.UpdateNotificationHandler;

/**
 * Created: 10/31/15 1:38 AM
 *
 * @author Yury Yakovlev
 */
public interface ApplicationsModuleProvider {

    void initialize(UpdateNotificationHandler handler);

    ApplicationsData getApplicationsData();

}
