package org.envtools.monitor.provider.applications.configurable.mapper;

import org.envtools.monitor.model.applications.*;
import org.envtools.monitor.provider.applications.configurable.model.VersionedApplicationPropertiesXml;
import org.envtools.monitor.provider.applications.configurable.model.VersionedApplicationXml;
import org.envtools.monitor.provider.applications.configurable.model.EnvironmentXml;
import org.envtools.monitor.provider.applications.configurable.model.PlatformXml;
import org.envtools.monitor.provider.applications.mock.model.MockApplication;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Michal Skuza on 07/07/16.
 */
public class ConfigurableModelMapperImpl implements ConfigurableModelMapper {
    @Override
    public ApplicationsData map(VersionedApplicationPropertiesXml configurableAppProperties) {
        ApplicationsData applicationsData = new ApplicationsData();
        applicationsData.setPlatforms(mapPlatforms(configurableAppProperties));
        return applicationsData;
    }

    private List<Platform> mapPlatforms(VersionedApplicationPropertiesXml applicationProperties) {
        List<Platform> platforms = new ArrayList<>();
        for (PlatformXml platformXml : applicationProperties.getPlatforms()) {
            platforms.add(convertPlatform(platformXml));
        }

        return platforms;
    }

    private ArrayList<Environment> mapEnvironments(PlatformXml platformXml) {
        ArrayList<Environment> environments = new ArrayList<Environment>();
        for (EnvironmentXml environmentXml : platformXml.getEnvironments()) {
            environments.add(convertEnvironment(environmentXml));
        }
        return environments;
    }

    private List<Application> mapApplications(EnvironmentXml environmentXml) {
        List<Application> applications = new ArrayList<>();
        for (VersionedApplicationXml versionedApplicationXml : environmentXml.getApplications()) {
            applications.add(convertApplication(versionedApplicationXml));
        }
        return applications;
    }

    private ArrayList<Application> mapHostees(VersionedApplicationXml versionedApplicationXml) {
        ArrayList<Application> hostees = new ArrayList<>();

        if (versionedApplicationXml.getHostees() != null) {
            for (VersionedApplicationXml hosteeApp : versionedApplicationXml.getHostees()) {
                hostees.add(convertHostee(hosteeApp));
            }
        }
        return hostees;
    }

    private Platform convertPlatform(PlatformXml platformXml) {
        Platform platformTmp = new Platform();
        platformTmp.setId(platformXml.getId());
        platformTmp.setName(platformXml.getName());
        platformTmp.setEnvironments(mapEnvironments(platformXml));
        return platformTmp;
    }

    private Environment convertEnvironment(EnvironmentXml environmentXml) {
        Environment environmentTmp = new Environment();
        environmentTmp.setId(environmentXml.getId());
        environmentTmp.setName(environmentXml.getName());
        environmentTmp.setApplications(mapApplications(environmentXml));
        return environmentTmp;
    }

    private Application convertApplication(VersionedApplicationXml versionedApplicationXml) {
        return new MockApplication.Builder()
                .id(versionedApplicationXml.getId())
                .name(versionedApplicationXml.getName())
                .host(versionedApplicationXml.getHost())
                .applicationType(versionedApplicationXml.getType())
                .port(versionedApplicationXml.getPort())
                .componentName(versionedApplicationXml.getComponentName())
                .url(versionedApplicationXml.getUrl())
                .status(versionedApplicationXml.getStatus())
                .version(versionedApplicationXml.getVersion())
                .processMemory(versionedApplicationXml.getProcessMemory())
                .build();
    }

    private Application convertHostee(VersionedApplicationXml hosteeApp) {
        return new MockApplication.Builder()
                .id(hosteeApp.getId())
                .name(hosteeApp.getName())
                .host(hosteeApp.getHost())
                .applicationType(hosteeApp.getType())
                .port(hosteeApp.getPort())
                .componentName(hosteeApp.getComponentName())
                .url(hosteeApp.getUrl())
                .status(hosteeApp.getStatus())
                .version(hosteeApp.getVersion())
                .processMemory(hosteeApp.getProcessMemory())
                .build();
    }
}