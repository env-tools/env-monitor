package org.envtools.monitor.module.configurable.applicationsMetadata;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Created by MSkuza on 2016-06-23.
 */
public class TagBasedProcessLookup extends ApplicationLookup {
    private List<String> includeTags;
    private List<String> excludeTags;

    public TagBasedProcessLookup() {
        this.includeTags = new ArrayList<>();
        this.excludeTags = new ArrayList<>();
    }

    @Override
    public List<String> getIncludeTags() {
        return includeTags;
    }

    @Override
    public List<String> getExcludeTags() {
        return excludeTags;
    }

    public void setIncludeTags(List<String> includeTags) {
        this.includeTags = includeTags;
    }

    public void setExcludeTags(List<String> excludeTags) {
        this.excludeTags = excludeTags;
    }

    public void includeTag(String tag) {
        this.includeTags.add(tag);
    }

    public void excludeTag(String tag) {
        this.excludeTags.add(tag);
    }
}
