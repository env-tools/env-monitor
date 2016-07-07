package org.envtools.monitor.module.configurable;

import org.envtools.monitor.model.applications.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Michal Skuza on 07/07/16.
 */
public class XmlToCoreModelMapper {
    public static ApplicationsData convertToApplicationsData(VersionedApplicationProperties applicationProperties) {
        ApplicationsData applicationsData = new ApplicationsData();

        List<Platform> platforms = new ArrayList<>();

        for (org.envtools.monitor.module.configurable.applicationsMetadata.Platform platform : applicationProperties.getPlatforms()) {

            ArrayList<Environment> environments = new ArrayList<>();

            for (org.envtools.monitor.module.configurable.applicationsMetadata.Environment environment : platform.getEnvironments()) {
                List<Application> applications = new ArrayList<>();

                for (VersionedApplication versionedApplication : environment.getApplications()) {
                    Application application = new Application();
                    application.setName(versionedApplication.getName());
                    application.setId(versionedApplication.getId());
                    application.setStatus(ApplicationStatus.RUNNING);

                    ArrayList<Application> hostees = new ArrayList<>();

                    if (versionedApplication.getHostees() != null) {
                        for (VersionedApplication hosteeApp : versionedApplication.getHostees()) {
                            Application hostee = new Application();
                            hostee.setName(hosteeApp.getName());
                            hostee.setId(hosteeApp.getId());
                            hostee.setStatus(ApplicationStatus.RUNNING);

                            hostees.add(hostee);
                        }
                    }

                    application.setHostees(hostees);
                    applications.add(application);
                }

                Environment environmentTmp = new Environment();
                environmentTmp.setId(environment.getId());
                environmentTmp.setName(environment.getName());
                environmentTmp.setApplications(applications);
                environments.add(environmentTmp);
            }


            Platform platformTmp = new Platform();
            platformTmp.setId(platform.getId());
            platformTmp.setName(platform.getName());
            platformTmp.setEnvironments(environments);
            platforms.add(platformTmp);
        }

        applicationsData.setPlatforms(platforms);


        return applicationsData;
    }
}
