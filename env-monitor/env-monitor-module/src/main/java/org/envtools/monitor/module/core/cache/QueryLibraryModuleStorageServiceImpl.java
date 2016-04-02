package org.envtools.monitor.module.core.cache;

import org.springframework.stereotype.Service;

/**
 * Created by sergey on 02.04.2016.
 */
@Service
public class QueryLibraryModuleStorageServiceImpl implements QueryLibraryModuleStorageService {

    @Override
    public String getPublicTree() {
        String jsonString = "{\"_public_\": {[{\"title\": \"test\", \"id\": \"12\", " +
                "\"categories\":[{\"title\": \"testa\"}], \"queries\":[{\"id\":\"15\", \"title\":\"test query\"}]]}}";

        return jsonString;
    }

    @Override
    public String getTreeByOwner(String owner) {
        String jsonString = "{\"owner_name\": {[{\"title\": \"test\", \"id\": \"12\", " +
                "\"categories\":[{\"title\": \"testa\"}], \"queries\":[{\"id\":\"15\", \"title\":\"test query\"}]]}}";

        return jsonString;
    }
}
