package org.envtools.monitor.module.core.selection;


import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import org.apache.commons.lang.StringUtils;

import com.jayway.jsonpath.*;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * Created: 12/5/15 1:22 AM
 *
 * @author Yury Yakovlev
 */
@Component
public class JSONExtractor implements Extractor<String, SimplePathSelector> {

    private static final Logger LOGGER = Logger.getLogger(JSONExtractor.class);

    private static final String ID_JSONPATH_PATTERN = "[?(@.id=='%s')]";
    private static final String JSONPATH_DELIM = ".";

    private static final String EMPTY_JSON_WITH_MESSAGE = "{ \"message\":\"%s\" }";
    private static final String EMPTY_JSON = "{ }";

    @Override
    public String extract(String source, SimplePathSelector selector) {

        String currentValidPath = "";

        boolean isRootSelectorSegment = true;
        Optional<String> currentJsonPart = Optional.empty();

        for (String segment : selector) {
            if (StringUtils.isEmpty(segment)) {
                continue;
            }

            if (isRootSelectorSegment) {
                currentJsonPart = getJsonPart(source, segment);
                if (!currentJsonPart.isPresent()) {
                    return noResult(String.format("No root property '%s' in json data object for path", segment));
                }
                currentValidPath = segment;
                isRootSelectorSegment = false;
            } else {

                LOGGER.debug(String.format("JSONExtractor.extract - probing non-root segment %s ",
                        segment));

                String propertyAccessPath = concatSegmentAsProperty(currentValidPath, segment);
                String idAccessPath = concatSegmentAsIdentifier(currentValidPath, segment);

                currentJsonPart = getJsonPart(source, propertyAccessPath);
                //Try considering current segment as a property

                if (!currentJsonPart.isPresent()) {
                    //No such property =>
                    //Try considering current segment as an Identifier of collection item
                    currentJsonPart = getJsonPart(source, idAccessPath);
                    if (!currentJsonPart.isPresent()) {
                        //Both attempts failed
                        return noResult(String.format("No nested property neither item with id '%s' in json data object for path %s",
                                segment, selector.getSelectorStr()));
                    } else {
                        currentJsonPart = Optional.of(firstJsonElement(currentJsonPart.get()));
                        currentValidPath = idAccessPath;
                    }
                } else {
                    currentValidPath = propertyAccessPath;
                    currentJsonPart = Optional.of(noNestedArrays(currentJsonPart.get()));
                }
            }

            LOGGER.debug(String.format("JSONExtractor.extract - extracted by path %s : %s ",
                    currentValidPath,
                    currentJsonPart));
        }

        return currentJsonPart.orElse(noResult("Extraction result was empty for selector " + selector.getSelectorStr()));
    }

    @Override
    public String emptyExtractionResult() {
        return EMPTY_JSON;
    }

    private String noResult(String message) {
        return String.format(EMPTY_JSON_WITH_MESSAGE, message);
    }

    private String noNestedArrays(String source) {
        source = StringUtils.trim(source);
        if (source.startsWith("[[") && source.endsWith("]]")) {
            return source.substring(1, source.length() - 1);
        }
        return source;
    }

    private String firstJsonElement(String source) {
        source = StringUtils.trim(source);
        int startIndex = source.indexOf("[");
        int endIndex = source.lastIndexOf("]");

        if (startIndex != -1 && endIndex != -1) {

            //TODO get normal regular JSONArray here??
            JSONObject json = JsonPath.read(source, "[0]");
            return json.toJSONString();
//            if (json.size() == 1) {
//                return source.substring(startIndex + 1, endIndex);
//            } else {
//                LOGGER.warn("JSONExtractor.firstJsonElement - " +
//                        "JSON being processed is not a 1-sized array : " + source);
//                return source;
//            }
        }

        LOGGER.warn("JSONExtractor.firstJsonElement - " +
                "JSON being processed is not an anonymous array : " + source);
        return source;
    }

    private Optional<String> getJsonPart(String source, String path) {
        Object jsonPart;
        try {
            //JSonPath always returns array of 1 element as result
            jsonPart = JsonPath.read(source, path);
        } catch (Exception e) {
            return Optional.empty();
        }

        if (null == jsonPart || jsonPart instanceof List && ((List<?>) jsonPart).isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(jsonPart.toString());
    }

    private String concatSegmentAsProperty(String validPath, String segment) {
        return validPath + JSONPATH_DELIM + segment;
    }

    private String concatSegmentAsIdentifier(String validPath, String segment) {
        return validPath + String.format(ID_JSONPATH_PATTERN, segment);
    }

}
