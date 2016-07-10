package configurable;

import org.envtools.monitor.model.applications.ApplicationsData;
import org.envtools.monitor.provider.applications.configurable.model.VersionedApplicationPropertiesXml;
import org.envtools.monitor.provider.applications.configurable.model.VersionedApplicationXml;
import org.envtools.monitor.provider.applications.configurable.model.*;
import org.envtools.monitor.provider.applications.configurable.mapper.ConfigurableModelMapperImpl;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by Michal Skuza on 07/07/16.
 */
public class ConfigurableModelMapperImplTest {

    @Test
    public void successfullyConvertToApplicationsData() throws Exception {
        VersionedApplicationPropertiesXml applicationProperties = createApplicationProperties();
        ConfigurableModelMapperImpl configurableModelMapper = new ConfigurableModelMapperImpl();
        ApplicationsData applicationsData = configurableModelMapper.map(applicationProperties);

        Assert.assertNotNull(applicationsData);
        Assert.assertTrue(!applicationsData.getPlatforms().isEmpty());
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
        LinkBasedVersionLookupXml versionLookup = new LinkBasedVersionLookupXml("/opt1/some_path/", "/opt1/some_other_path/");
        metadataXml.setVersionLookup(versionLookup);
        standardDevServer.setMetadata(metadataXml);
        platformXml.addEnvironment(environmentXml);
        properties.add(platformXml);

        return properties;
    }
}