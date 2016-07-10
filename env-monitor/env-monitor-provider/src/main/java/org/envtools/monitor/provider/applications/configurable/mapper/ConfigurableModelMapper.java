package org.envtools.monitor.provider.applications.configurable.mapper;

import org.envtools.monitor.model.applications.ApplicationsData;
import org.envtools.monitor.provider.applications.configurable.model.VersionedApplicationPropertiesXml;

/**
 * Created by Michal Skuza on 08/07/16.
 */
public interface ConfigurableModelMapper {

    ApplicationsData map(VersionedApplicationPropertiesXml configurableAppProperties);

}
