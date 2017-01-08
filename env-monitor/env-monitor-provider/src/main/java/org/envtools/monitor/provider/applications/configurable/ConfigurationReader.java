package org.envtools.monitor.provider.applications.configurable;

import org.envtools.monitor.common.jaxb.JaxbHelper;
import org.envtools.monitor.provider.applications.configurable.model.VersionedApplicationPropertiesXml;
import org.springframework.stereotype.Service;

import javax.xml.bind.JAXBException;
import java.io.File;

/**
 * Created by Michal Skuza on 2016-06-22.
 */

public class ConfigurationReader {

    public VersionedApplicationPropertiesXml readConfiguration(String marshalledData) {
        try {
            return JaxbHelper.unmarshallFromString(marshalledData, VersionedApplicationPropertiesXml.class);
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }
    }

    public VersionedApplicationPropertiesXml readConfigurationFromFile(File file) {
        try {
            return JaxbHelper.unmarshallFromFile(file, VersionedApplicationPropertiesXml.class, null);
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }
    }
}