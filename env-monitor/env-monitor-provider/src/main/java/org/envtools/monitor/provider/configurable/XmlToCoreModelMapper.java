package org.envtools.monitor.provider.configurable;

import org.envtools.monitor.model.applications.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Michal Skuza on 07/07/16.
 */
public class XmlToCoreModelMapper {
    public static ApplicationsData convertToApplicationsData(VersionedApplicationProperties applicationProperties) {
        ApplicationsData applicationsData = new ApplicationsData();
        applicationsData.setPlatforms(mapPlatforms(applicationProperties));
        return applicationsData;
    }

    private static List<Platform>  mapPlatforms(VersionedApplicationProperties applicationProperties) {
        List<Platform> platforms = new ArrayList<>();
        for (org.envtools.monitor.provider.configurable.applicationsMetadata.Platform platform : applicationProperties.getPlatforms()) {
            platforms.add(convertPlatform(platform));
        }

        return platforms;
    }

    private static ArrayList<Environment> mapEnvironments(org.envtools.monitor.provider.configurable.applicationsMetadata.Platform platform) {
        ArrayList<Environment> environments = new ArrayList<Environment>();
        for (org.envtools.monitor.provider.configurable.applicationsMetadata.Environment environment : platform.getEnvironments()) {
            environments.add(convertEnvironment(environment));
        }
        return environments;
    }

    private static List<Application> mapApplications(org.envtools.monitor.provider.configurable.applicationsMetadata.Environment environment) {
        List<Application> applications = new ArrayList<>();
        for (VersionedApplication versionedApplication : environment.getApplications()) {
            applications.add(convertApplication(versionedApplication));
        }
        return applications;
    }

    private static ArrayList<Application> mapHostees(VersionedApplication versionedApplication) {
        ArrayList<Application> hostees = new ArrayList<>();

        if (versionedApplication.getHostees() != null) {
            for (VersionedApplication hosteeApp : versionedApplication.getHostees()) {
                hostees.add(convertHostee(hosteeApp));
            }
        }
        return hostees;
    }

    private static Platform convertPlatform(org.envtools.monitor.provider.configurable.applicationsMetadata.Platform platform) {
        Platform platformTmp = new Platform();
        platformTmp.setId(platform.getId());
        platformTmp.setName(platform.getName());
        platformTmp.setEnvironments(mapEnvironments(platform));
        return platformTmp;
    }

    private static Environment convertEnvironment(org.envtools.monitor.provider.configurable.applicationsMetadata.Environment environment) {
        Environment environmentTmp = new Environment();
        environmentTmp.setId(environment.getId());
        environmentTmp.setName(environment.getName());
        environmentTmp.setApplications(mapApplications(environment));
        return environmentTmp;
    }

    private static Application convertApplication(VersionedApplication versionedApplication) {
        Application application = new Application();
        application.setName(versionedApplication.getName());
        application.setId(versionedApplication.getId());
        application.setStatus(ApplicationStatus.RUNNING);
        application.setHostees(mapHostees(versionedApplication));
        return application;
    }

    private static Application convertHostee(VersionedApplication hosteeApp) {
        Application hostee = new Application();
        hostee.setName(hosteeApp.getName());
        hostee.setId(hosteeApp.getId());
        hostee.setStatus(ApplicationStatus.RUNNING);
        return hostee;
    }
}
