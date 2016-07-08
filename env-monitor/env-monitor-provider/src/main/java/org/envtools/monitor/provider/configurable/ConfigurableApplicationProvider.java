package org.envtools.monitor.provider.configurable;

import org.envtools.monitor.common.jaxb.JaxbHelper;

import javax.xml.bind.JAXBException;

/**
 * Created by Michal Skuza on 2016-06-22.
 */
public class ConfigurableApplicationProvider {
    public VersionedApplicationPropertiesXml createVersionedApplication(String marshalledData) {
        try {
        return JaxbHelper.unmarshallFromString(marshalledData, VersionedApplicationPropertiesXml.class);
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }
    }
}
