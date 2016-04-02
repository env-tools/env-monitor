package org.envtools.monitor.module.core.cache;

import org.springframework.stereotype.Service;

/**
 * Created by sergey on 02.04.2016.
 */
@Service
public class QueryLibraryModuleStorageServiceImpl implements QueryLibraryModuleStorageService {

    @Override
    public String getPublicTree() {
        String jsonString = "{\n" +
                "      \"title\": \"public\",\n" +
                "      \"queries\": [],\n" +
                "      \"categories\": [\n" +
                "        {\n" +
                "          \"id\": 5,\n" +
                "          \"title\": \"First public category\",\n" +
                "          \"categories\": [{\n" +
                "            \"id\": 6,\n" +
                "            \"title\": \"Second private category\",\n" +
                "            \"queries\": [\n" +
                "              {\n" +
                "                \"id\": 1,\n" +
                "                \"title\": \"Query 1\"\n" +
                "              },\n" +
                "              {\n" +
                "                \"id\": 2,\n" +
                "                \"title\": \"Query 2\"\n" +
                "              },\n" +
                "              {\n" +
                "                \"id\": 3,\n" +
                "                \"title\": \"Query 3\"\n" +
                "              }\n" +
                "            ],\n" +
                "            \"categories\": []\n" +
                "          }]\n" +
                "        }\n" +
                "      ]\n" +
                "    }";

        return jsonString;
    }

    @Override
    public String getTreeByOwner(String owner) {
        String jsonString = "{\n" +
                "      \"title\": \"private\",\n" +
                "      \"categories\": [\n" +
                "        {\n" +
                "          \"id\": 2,\n" +
                "          \"title\": \"First private category\",\n" +
                "          \"queries\": [\n" +
                "            {\n" +
                "              \"id\": 1,\n" +
                "              \"title\": \"Query 1\"\n" +
                "            },\n" +
                "            {\n" +
                "              \"id\": 2,\n" +
                "              \"title\": \"Query 2\"\n" +
                "            },\n" +
                "            {\n" +
                "              \"id\": 3,\n" +
                "              \"title\": \"Query 3\"\n" +
                "            }\n" +
                "          ],\n" +
                "          \"categories\": []\n" +
                "        },\n" +
                "        {\n" +
                "          \"id\": 3,\n" +
                "          \"title\": \"Second private category\",\n" +
                "          \"queries\": [],\n" +
                "          \"categories\": [\n" +
                "            {\n" +
                "              \"id\": 4,\n" +
                "              \"title\": \"Third private category\",\n" +
                "              \"queries\": [],\n" +
                "              \"categories\": []\n" +
                "            }\n" +
                "          ]\n" +
                "        }\n" +
                "      ]\n" +
                "    }";

        return jsonString;
    }
}
