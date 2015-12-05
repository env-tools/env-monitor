package org.envtools.monitor.module.core.selection;

import org.apache.commons.lang.StringUtils;

import com.jayway.jsonpath.*;

import java.util.List;
import java.util.Optional;

/**
 * Created: 12/5/15 1:22 AM
 *
 * @author Yury Yakovlev
 */
public class JSONExtractor implements Extractor<String, SimplePathSelector> {

    private static final String ID_JSONPATH_PATTERN = "[?(@.id=='%s')]";
    private static final String JSONPATH_DELIM = ".";

    private static final String EMPTY_JSON_WITH_MESSAGE = "{ \"message\":\"%s\" }";

    @Override
    public String extract(String source, SimplePathSelector selector) {

        String validPath = "";

        boolean isRootSegment = true;
        Optional<String> currentPart = Optional.empty();

        for (String segment : selector) {
            if (StringUtils.isEmpty(segment)) {
                continue;
            }

            if (isRootSegment) {
                currentPart = getPart(source, segment);
                if (!currentPart.isPresent()) {
                    return noResult(String.format("No root property '%s' in json data object for path", segment));
                }
                validPath = segment;
                isRootSegment = false;
            } else {

                String propertyAccessPath = concatAsProperty(validPath, segment);
                String idAccessPath = concatAsIdentifier(validPath, segment);

                currentPart = getPart(source, propertyAccessPath);

                if (!currentPart.isPresent()) {
                    currentPart = getPart(source, idAccessPath);
                    if (!currentPart.isPresent()) {
                        return noResult(String.format("No nested property neither item with id '%s' in json data object for path %s",
                                segment, selector.getSelectorStr()));
                    } else {
                        validPath = unnest(idAccessPath);
                    }
                } else {
                    validPath = propertyAccessPath;
                }
            }
        }

        return currentPart.get();
    }

    private String unnest(String singleElementArray) {
        //TODO implement
        return singleElementArray;
    }

    private String noResult(String message) {
        return String.format(EMPTY_JSON_WITH_MESSAGE, message);
    }

    private Optional<String> getPart(String source, String path) {
        Object jsonPart;
        try {
            jsonPart = JsonPath.read(source, path);
        } catch (InvalidPathException e) {
            return Optional.empty();
        }

        if (null == jsonPart || jsonPart instanceof List && ((List<?>) jsonPart).isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(jsonPart.toString());
    }

    private String concatAsProperty(String validPath, String segment) {
        return validPath + JSONPATH_DELIM + segment;
    }

    private String concatAsIdentifier(String validPath, String segment) {
        return validPath + String.format(ID_JSONPATH_PATTERN, segment);
    }

}
