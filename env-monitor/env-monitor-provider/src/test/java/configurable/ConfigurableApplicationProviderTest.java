package configurable;

import org.apache.commons.io.FileUtils;
import org.envtools.monitor.common.jaxb.JaxbHelper;
import org.envtools.monitor.provider.applications.configurable.ConfigurationReader;
import org.envtools.monitor.provider.applications.configurable.model.VersionedApplicationXml;
import org.envtools.monitor.provider.applications.configurable.model.VersionedApplicationPropertiesXml;
import org.envtools.monitor.provider.applications.configurable.model.*;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.unitils.reflectionassert.ReflectionAssert;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.IOException;

/**
 * Created by Michal Skuza on 2016-06-23.
 */
public class ConfigurableApplicationProviderTest {

    @Test
    public void testMarshalling() {
        VersionedApplicationPropertiesXml applicationProperties = createApplicationProperties();
        String marshalledApplicationProperties = marshalToXml(applicationProperties);

        System.out.println(marshalledApplicationProperties);

        ConfigurationReader provider = new ConfigurationReader();
        VersionedApplicationPropertiesXml unmarshalledApplicationProperties = provider.readConfiguration(marshalledApplicationProperties);

        Assert.assertNotNull(unmarshalledApplicationProperties);
        ReflectionAssert.assertReflectionEquals(
                applicationProperties, unmarshalledApplicationProperties);
    }

    @Ignore
    @Test
    public void testFromExternalFile() throws IOException {
        String xmlFileContents = FileUtils.readFileToString(
                new File(
                        //"" //Put file path here
                        "C:\\work\\env-monitor\\app-module-citi-test\\applications-metadata.xml"
                ));

        System.out.println(xmlFileContents);

        ConfigurationReader provider = new ConfigurationReader();
        VersionedApplicationPropertiesXml unmarshalledApplicationProperties = provider.readConfiguration(xmlFileContents);

        Assert.assertNotNull(unmarshalledApplicationProperties);
        System.out.println(String.format("Unmarshalled object: %s", unmarshalledApplicationProperties));

    }

    private String marshalToXml(VersionedApplicationPropertiesXml applicationProperties) {
        VersionedApplicationPropertiesXml versionedApplicationPropertiesXml = createApplicationProperties();
        try {
            return JaxbHelper.marshallToString(versionedApplicationPropertiesXml);
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }
    }

    private VersionedApplicationPropertiesXml createApplicationProperties() {
        VersionedApplicationPropertiesXml properties = new VersionedApplicationPropertiesXml();
        PlatformXml platformXml = new PlatformXml("ULTRON", "ultron");
        EnvironmentXml environmentXml = new EnvironmentXml("Standard DEV (DEV 1)", "standard_dev");
        VersionedApplicationXml standardDevServer = new VersionedApplicationXml("standardDevServer", "ULTRON Server", "ULTRON Server", "ULTRON.net", 999, "-", "-");
        standardDevServer.addHostee(new VersionedApplicationXml("standardDevServer", "IRON Server", "IRON Server", "IRON.net", 999, "-", "-"));
        environmentXml.addApplication(standardDevServer);
        TagBasedProcessLookupXml applicationLookupXml = new TagBasedProcessLookupXml();
        applicationLookupXml.includeTag("ultron.component.name=container1");
        applicationLookupXml.includeTag("ultron.component.name=container2");
        applicationLookupXml.excludeTag("ultron.component.name=container3");
        applicationLookupXml.excludeTag("ultron.component.name=container4");
        MetadataXml metadataXml = new MetadataXml();
        metadataXml.setApplicationLookupXml(applicationLookupXml);
        LinkBasedVersionLookupXml versionLookup = new LinkBasedVersionLookupXml("/opt1/some_path/",
                "/opt1/some_other_path/(.*)");
        metadataXml.setVersionLookup(versionLookup);
        standardDevServer.setMetadata(metadataXml);
        platformXml.addEnvironment(environmentXml);
        properties.add(platformXml);

        return properties;
    }
}