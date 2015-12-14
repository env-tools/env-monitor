package org.envtools.monitor.module.core.selection;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.envtools.monitor.module.core.selection.exception.IllegalSelectorException;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * Created: 12/5/15 2:02 AM
 *
 * @author Yury Yakovlev
 */
public class SimplePathSelector implements Iterable<String>{

    private static final String SEGMENT_PATTERN = "^[a-zA-Z0-9\\-_]*$";
    private static final String DEFAULT_SEGMENT_SEPARATOR = "/";

    private String selectorStr;

    private List<String> segments;

    public SimplePathSelector(String selectorString, String selectorSegmentSeparator) throws IllegalSelectorException {
        this.selectorStr = selectorString;
        this.segments = Arrays.asList(
                StringUtils.split(selectorString, selectorSegmentSeparator))
                .stream()
                .filter(s -> !Optional.ofNullable(s).orElse("").isEmpty())
                .collect(Collectors.<String>toList());

        validateSegments();
    }

    public static SimplePathSelector of(String selectorString) throws IllegalSelectorException{
        return new SimplePathSelector(selectorString, DEFAULT_SEGMENT_SEPARATOR);
    }

    private void validateSegments() throws IllegalSelectorException {
       for (String segment : this) {
           if (!segment.matches(SEGMENT_PATTERN)) {
               throw new IllegalSelectorException(
                       String.format(
                               "Selector %s: segment '%s' is not valid (must match pattern %s)",
                       selectorStr, segment, SEGMENT_PATTERN));
           }
       }
    }

    public String getSelectorStr() {
        return selectorStr;
    }

    @Override
    public Iterator<String> iterator() {
        return segments.iterator();
    }

    @Override
    public void forEach(Consumer<? super String> action) {
        segments.forEach(action);
    }

    @Override
    public Spliterator<String> spliterator() {
        return segments.spliterator();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).
                append("selectorStr", selectorStr).
                append("segments", segments).
                toString();
    }
}
