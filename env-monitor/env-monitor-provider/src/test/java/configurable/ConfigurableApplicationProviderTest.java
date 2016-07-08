package configurable;

import org.envtools.monitor.common.jaxb.JaxbHelper;
import org.envtools.monitor.provider.configurable.ConfigurableApplicationProvider;
import org.envtools.monitor.provider.configurable.VersionedApplicationXml;
import org.envtools.monitor.provider.configurable.VersionedApplicationPropertiesXml;
import org.envtools.monitor.provider.configurable.metadata.*;
import org.junit.Assert;
import org.junit.Test;

import javax.xml.bind.JAXBException;

/**
 * Created by Michal Skuza on 2016-06-23.
 */
public class ConfigurableApplicationProviderTest {

    @Test
    public void testMarshalling() {
        String marshalledData = createXmlData();
        System.out.println(marshalledData);
        ConfigurableApplicationProvider provider = new ConfigurableApplicationProvider();
        VersionedApplicationPropertiesXml versionedApplication = provider.createVersionedApplication(marshalledData);

        Assert.assertNotNull(versionedApplication);
    }

    private String createXmlData() {
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
        ApplicationLookupXml applicationLookupXml = new TagBasedProcessLookupXml();
        applicationLookupXml.includeTag("ultron.component.name=container1");
        applicationLookupXml.includeTag("ultron.component.name=container2");
        applicationLookupXml.excludeTag("ultron.component.name=container3");
        applicationLookupXml.excludeTag("ultron.component.name=container4");
        MetadataXml metadataXml = new MetadataXml();
        metadataXml.setApplicationLookupXml(applicationLookupXml);
        LinkBasedVersionLookupXml versionLookup = new LinkBasedVersionLookupXml("/opt1/some_path/", "/opt1/some_other_path/");
        metadataXml.setVersionLookup(versionLookup);
        standardDevServer.setMetadata(metadataXml);
        platformXml.addEnvironment(environmentXml);
        properties.add(platformXml);

        return properties;
    }
}