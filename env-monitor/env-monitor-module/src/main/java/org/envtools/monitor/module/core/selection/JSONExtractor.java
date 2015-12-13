package org.envtools.monitor.module.core.selection;

import org.apache.commons.lang.StringUtils;

import com.jayway.jsonpath.*;
import org.apache.log4j.Logger;

import java.util.List;
import java.util.Optional;

/**
 * Created: 12/5/15 1:22 AM
 *
 * @author Yury Yakovlev
 */
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
                }
            }

            LOGGER.debug(String.format("JSONExtractor.extract - extracted by path %s : %s ",
                    currentValidPath,
                    currentJsonPart.get()));
        }

        return currentJsonPart.get();
    }

    @Override
    public String emptyExtractionResult() {
        return EMPTY_JSON;
    }

    private String noResult(String message) {
        return String.format(EMPTY_JSON_WITH_MESSAGE, message);
    }

    private String firstJsonElement(String source) {
        return source + "[0]";
    }

    private Optional<String> getJsonPart(String source, String path) {
        Object jsonPart;
        try {
            //JSonPath always returns array of 1 element as result
            jsonPart = JsonPath.read(source, path);
        } catch (InvalidPathException e) {
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
