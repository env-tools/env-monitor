package org.envtools.monitor.module.configurable;

import org.envtools.monitor.common.jaxb.JaxbHelper;

import javax.xml.bind.JAXBException;

/**
 * Created by Michal Skuza on 2016-06-22.
 */
public class ConfigurableApplicationProvider {
    public VersionedApplicationProperties createVersionedApplication(String marshalledData) {
        try {
        return JaxbHelper.unmarshallFromString(marshalledData, VersionedApplicationProperties.class);
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }
    }
}
