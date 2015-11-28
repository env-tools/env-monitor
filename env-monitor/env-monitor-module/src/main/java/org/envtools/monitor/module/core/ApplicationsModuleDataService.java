package org.envtools.monitor.module.core;

import org.envtools.monitor.model.applications.ApplicationsData;

/**
 * Created: 11/28/15 3:24 AM
 *
 * @author Yury Yakovlev
 */
public interface ApplicationsModuleDataService {

    String extractSerializedPartBySelector(String selector);

    void store(String serializedApplicationsData);

}
