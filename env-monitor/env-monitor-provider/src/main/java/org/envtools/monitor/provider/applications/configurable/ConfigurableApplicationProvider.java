package org.envtools.monitor.provider.applications.configurable;

import org.envtools.monitor.common.jaxb.JaxbHelper;
import org.envtools.monitor.provider.applications.configurable.model.VersionedApplicationPropertiesXml;

import javax.xml.bind.JAXBException;

/**
 * Created by Michal Skuza on 2016-06-22.
 */
public class ConfigurableApplicationProvider {

    public VersionedApplicationPropertiesXml readConfiguration(String marshalledData) {
        try {
            return JaxbHelper.unmarshallFromString(marshalledData, VersionedApplicationPropertiesXml.class);
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }
    }

}
