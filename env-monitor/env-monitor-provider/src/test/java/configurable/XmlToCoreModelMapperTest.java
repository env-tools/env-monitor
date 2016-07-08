package configurable;

import org.envtools.monitor.model.applications.ApplicationsData;
import org.envtools.monitor.provider.configurable.VersionedApplication;
import org.envtools.monitor.provider.configurable.VersionedApplicationProperties;
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
        VersionedApplicationProperties applicationProperties = createApplicationProperties();
        ApplicationsData applicationsData = XmlToCoreModelMapper.convertToApplicationsData(applicationProperties);

        Assert.assertNotNull(applicationsData);
        Assert.assertTrue(!applicationsData.getPlatforms().isEmpty());
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