package org.envtools.monitor.module.core.cache;

import org.envtools.monitor.model.applications.ApplicationsData;

/**
 * Created: 11/28/15 3:24 AM
 *
 * @author Yury Yakovlev
 */
public interface ApplicationsModuleStorageService {

    String extractPartBySelector(String selector);

    void storeFull(String data);

}
