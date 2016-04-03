package org.envtools.monitor.module.core.cache;

import org.envtools.monitor.model.messaging.content.AbstractContent;
import org.envtools.monitor.model.messaging.content.MapContent;

import java.util.Map;

/**
 * Created: 02/04/16
 *
 * @author sergey
 */
public interface QueryLibraryModuleStorageService {

    String getPublicTree();

    String getTreeByOwner(String owner);

    void storeFull(Map<String, String> data);
}
