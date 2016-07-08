package configurable;

import org.envtools.monitor.common.jaxb.JaxbHelper;
import org.envtools.monitor.provider.configurable.ConfigurableApplicationProvider;
import org.envtools.monitor.provider.configurable.VersionedApplication;
import org.envtools.monitor.provider.configurable.VersionedApplicationProperties;
import org.envtools.monitor.provider.configurable.applicationsMetadata.*;
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

        ConfigurableApplicationProvider provider = new ConfigurableApplicationProvider();
        VersionedApplicationProperties versionedApplication = provider.createVersionedApplication(marshalledData);

        Assert.assertNotNull(versionedApplication);
    }

    private String createXmlData() {
        VersionedApplicationProperties versionedApplicationProperties = createApplicationProperties();
        try {
            return JaxbHelper.marshallToString(versionedApplicationProperties);
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }
    }

    private VersionedApplicationProperties createApplicationProperties() {
        VersionedApplicationProperties properties = new VersionedApplicationProperties();
        Platform platform = new Platform("ULTRON", "ultron");
        Environment environment = new Environment("Standard DEV (DEV 1)", "standard_dev");
        VersionedApplication standardDevServer = new VersionedApplication("standardDevServer", "ULTRON Server", "ULTRON Server", "ULTRON.net", 999, "-", "-");
        environment.addApplication(standardDevServer);
        ApplicationLookup applicationLookup = new TagBasedProcessLookup();
        applicationLookup.includeTag("ultron.component.name=container1");
        applicationLookup.includeTag("ultron.component.name=container2");
        applicationLookup.excludeTag("ultron.component.name=container3");
        applicationLookup.excludeTag("ultron.component.name=container4");
        Metadata metadata = new Metadata();
        metadata.setApplicationLookup(applicationLookup);
        LinkBasedVersionLookup versionLookup = new LinkBasedVersionLookup("/opt1/some_path/", "/opt1/some_other_path/");
        metadata.setVersionLookup(versionLookup);
        standardDevServer.setMetadata(metadata);
        platform.addEnvironment(environment);
        properties.add(platform);

        return properties;
    }
}