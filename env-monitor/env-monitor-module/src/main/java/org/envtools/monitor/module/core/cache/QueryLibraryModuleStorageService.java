package org.envtools.monitor.module.core.cache;

/**
 * Created: 02/04/16
 *
 * @author sergey
 */
public interface QueryLibraryModuleStorageService {

    String getPublicTree();

    String getTreeByOwner(String owner);
}
