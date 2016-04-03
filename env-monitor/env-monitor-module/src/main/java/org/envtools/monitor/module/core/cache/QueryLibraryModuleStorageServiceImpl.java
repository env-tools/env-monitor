package org.envtools.monitor.module.core.cache;

import org.envtools.monitor.model.messaging.content.AbstractContent;
import org.envtools.monitor.model.messaging.content.MapContent;
import org.envtools.monitor.module.ModuleConstants;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by sergey on 02.04.2016.
 */
@Service
public class QueryLibraryModuleStorageServiceImpl implements QueryLibraryModuleStorageService {

    private MapContent serializedQueryLibraryData;

    @Override
    public String getPublicTree() {
        Map<String, String> mc = (HashMap<String, String>) serializedQueryLibraryData.getValue();

        return mc.get(ModuleConstants.OWNER_NULL);
    }

    @Override
    public String getTreeByOwner(String owner) {
        Map<String, String> mc = (HashMap<String, String>) serializedQueryLibraryData.getValue();

        return mc.get(owner);
    }

    @Override
    public void storeFull(AbstractContent data) {
        this.serializedQueryLibraryData = (MapContent) data;
    }
}
