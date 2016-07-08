package org.envtools.monitor.provider.configurable.metadata;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Michal Skuza on 2016-06-23.
 */
public class TagBasedProcessLookupXml extends ApplicationLookupXml {
    private List<String> includeTags;
    private List<String> excludeTags;

    public TagBasedProcessLookupXml() {
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
