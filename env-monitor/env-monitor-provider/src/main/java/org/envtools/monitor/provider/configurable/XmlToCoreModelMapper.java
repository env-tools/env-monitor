package org.envtools.monitor.provider.configurable;

import org.envtools.monitor.model.applications.*;
import org.envtools.monitor.provider.configurable.metadata.EnvironmentXml;
import org.envtools.monitor.provider.configurable.metadata.PlatformXml;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Michal Skuza on 07/07/16.
 */
public class XmlToCoreModelMapper {
    public static ApplicationsData convertToApplicationsData(VersionedApplicationPropertiesXml applicationProperties) {
        ApplicationsData applicationsData = new ApplicationsData();
        applicationsData.setPlatforms(mapPlatforms(applicationProperties));
        return applicationsData;
    }

    private static List<Platform>  mapPlatforms(VersionedApplicationPropertiesXml applicationProperties) {
        List<Platform> platforms = new ArrayList<>();
        for (PlatformXml platformXml : applicationProperties.getPlatforms()) {
            platforms.add(convertPlatform(platformXml));
        }

        return platforms;
    }

    private static ArrayList<Environment> mapEnvironments(PlatformXml platformXml) {
        ArrayList<Environment> environments = new ArrayList<Environment>();
        for (EnvironmentXml environmentXml : platformXml.getEnvironments()) {
            environments.add(convertEnvironment(environmentXml));
        }
        return environments;
    }

    private static List<Application> mapApplications(EnvironmentXml environmentXml) {
        List<Application> applications = new ArrayList<>();
        for (VersionedApplicationXml versionedApplicationXml : environmentXml.getApplications()) {
            applications.add(convertApplication(versionedApplicationXml));
        }
        return applications;
    }

    private static ArrayList<Application> mapHostees(VersionedApplicationXml versionedApplicationXml) {
        ArrayList<Application> hostees = new ArrayList<>();

        if (versionedApplicationXml.getHostees() != null) {
            for (VersionedApplicationXml hosteeApp : versionedApplicationXml.getHostees()) {
                hostees.add(convertHostee(hosteeApp));
            }
        }
        return hostees;
    }

    private static Platform convertPlatform(PlatformXml platformXml) {
        Platform platformTmp = new Platform();
        platformTmp.setId(platformXml.getId());
        platformTmp.setName(platformXml.getName());
        platformTmp.setEnvironments(mapEnvironments(platformXml));
        return platformTmp;
    }

    private static Environment convertEnvironment(EnvironmentXml environmentXml) {
        Environment environmentTmp = new Environment();
        environmentTmp.setId(environmentXml.getId());
        environmentTmp.setName(environmentXml.getName());
        environmentTmp.setApplications(mapApplications(environmentXml));
        return environmentTmp;
    }

    private static Application convertApplication(VersionedApplicationXml versionedApplicationXml) {
        Application application = new Application();
        application.setName(versionedApplicationXml.getName());
        application.setId(versionedApplicationXml.getId());
        application.setStatus(ApplicationStatus.RUNNING);
        application.setHostees(mapHostees(versionedApplicationXml));
        return application;
    }

    private static Application convertHostee(VersionedApplicationXml hosteeApp) {
        Application hostee = new Application();
        hostee.setName(hosteeApp.getName());
        hostee.setId(hosteeApp.getId());
        hostee.setStatus(ApplicationStatus.RUNNING);
        return hostee;
    }
}
