package org.envtools.monitor.provider.configurable.metadata;

import javax.xml.bind.annotation.*;
import java.util.List;

/**
 * Created by Michal Skuza on 2016-06-23.
 */

@XmlRootElement(name = "metadata")
@XmlAccessorType(value = XmlAccessType.PROPERTY)
public abstract class ApplicationLookupXml {

    @XmlElementWrapper
    @XmlElement(name = "tag")
    public abstract List<String> getIncludeTags();

    @XmlElementWrapper
    @XmlElement(name = "tag")
    public abstract List<String> getExcludeTags();

    public abstract void includeTag(String tag);
    public abstract void excludeTag(String tag);


}
