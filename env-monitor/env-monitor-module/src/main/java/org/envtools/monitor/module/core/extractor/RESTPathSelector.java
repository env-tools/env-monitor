package org.envtools.monitor.module.core.extractor;

import org.apache.commons.lang.StringUtils;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Spliterator;
import java.util.function.Consumer;

/**
 * Created: 12/5/15 2:02 AM
 *
 * @author Yury Yakovlev
 */
public class RESTPathSelector implements Iterable<String>{

    private String selectorSegmentSeparator;

    private List<String> segments;

    public RESTPathSelector(String selectorString, String selectorSegmentSeparator) {
        this.selectorSegmentSeparator = selectorSegmentSeparator;
        this.segments = Arrays.asList(StringUtils.split(selectorString, selectorSegmentSeparator));
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

    public void setSelectorSegmentSeparator(String selectorSegmentSeparator) {
        this.selectorSegmentSeparator = selectorSegmentSeparator;
    }
}
