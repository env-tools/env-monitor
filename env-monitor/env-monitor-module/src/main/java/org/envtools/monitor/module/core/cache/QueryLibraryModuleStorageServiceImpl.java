package org.envtools.monitor.module.core.cache;

import org.envtools.monitor.module.ModuleConstants;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Created by sergey on 02.04.2016.
 */
@Service
public class QueryLibraryModuleStorageServiceImpl implements QueryLibraryModuleStorageService {

    private Map<String, String> serializedQueryLibraryData;

    @Override
    public String getPublicTree() {
        return serializedQueryLibraryData.get(ModuleConstants.OWNER_NULL);
    }

    @Override
    public String getTreeByOwner(String owner) {
        String categories = serializedQueryLibraryData.get(owner);
        return categories != null ? categories : "{}";
    }

    @Override
    public void storeFull(Map<String, String> data) {
        this.serializedQueryLibraryData = data;
    }
}
