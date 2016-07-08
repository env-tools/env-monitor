package configurable;

import org.envtools.monitor.model.applications.ApplicationsData;
import org.envtools.monitor.provider.configurable.VersionedApplicationXml;
import org.envtools.monitor.provider.configurable.VersionedApplicationPropertiesXml;
import org.envtools.monitor.provider.configurable.XmlToCoreModelMapper;
import org.envtools.monitor.provider.configurable.metadata.*;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by Michal Skuza on 07/07/16.
 */
public class XmlToCoreModelMapperTest {

    @Test
    public void successfullyConvertToApplicationsData() throws Exception {
        VersionedApplicationPropertiesXml applicationProperties = createApplicationProperties();
        ApplicationsData applicationsData = XmlToCoreModelMapper.convertToApplicationsData(applicationProperties);

        Assert.assertNotNull(applicationsData);
        Assert.assertTrue(!applicationsData.getPlatforms().isEmpty());
    }

    private VersionedApplicationPropertiesXml createApplicationProperties() {
        VersionedApplicationPropertiesXml properties = new VersionedApplicationPropertiesXml();
        PlatformXml platformXml = new PlatformXml("ULTRON", "ultron");
        EnvironmentXml environmentXml = new EnvironmentXml("Standard DEV (DEV 1)", "standard_dev");
        VersionedApplicationXml standardDevServer = new VersionedApplicationXml("standardDevServer", "ULTRON Server", "ULTRON Server", "ULTRON.net", 999, "-", "-");
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