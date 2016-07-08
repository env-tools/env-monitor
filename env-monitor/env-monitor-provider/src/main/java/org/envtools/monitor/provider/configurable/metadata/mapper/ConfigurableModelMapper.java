package org.envtools.monitor.provider.configurable.metadata.mapper;

import org.envtools.monitor.model.applications.ApplicationsData;
import org.envtools.monitor.provider.configurable.VersionedApplicationPropertiesXml;

/**
 * Created by Michal Skuza on 08/07/16.
 */
public interface ConfigurableModelMapper {
    ApplicationsData map(VersionedApplicationPropertiesXml configurableAppProperties);
}
