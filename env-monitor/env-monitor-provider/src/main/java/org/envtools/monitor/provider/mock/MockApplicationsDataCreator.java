package org.envtools.monitor.provider.mock;

import org.envtools.monitor.model.applications.Application;
import org.envtools.monitor.model.applications.ApplicationStatus;
import org.envtools.monitor.model.applications.Environment;
import org.envtools.monitor.model.applications.Platform;
import org.envtools.monitor.provider.mock.model.MockApplication;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created: 11/27/15 4:28 PM
 *
 * @author Yury Yakovlev
 */
public class MockApplicationsDataCreator {

    public static List<Platform> createPlatforms() {
        Platform platform1 = new Platform();
        platform1.setId("PLAT1");
        platform1.setName("Platform 1");
        platform1.setEnvironments(createPlatform1Environments());

        Platform platform2 = new Platform();
        platform2.setId("PLAT2");
        platform2.setName("Platform 2");
        platform2.setEnvironments(createPlatform2Environments());
        return Arrays.asList(platform1, platform2);
    }

    private static List<Environment> createPlatform1Environments() {
        Environment environment1 = new Environment();
        environment1.setId("PL1-ENV1");
        environment1.setName("Env 1 of platform 1");
        environment1.setApplications(createApplications("1-1"));

        Environment environment2 = new Environment();
        environment2.setId("PL1-ENV2");
        environment2.setName("Env 2 of platform 1");
        environment2.setApplications(createApplications("2-1"));

        return Arrays.asList(environment1, environment2);
    }

    private static List<Environment> createPlatform2Environments() {
        Environment environment1 = new Environment();
        environment1.setId("PL2-ENV1");
        environment1.setName("Env 1 of platform 2");
        environment1.setApplications(createApplications("1-2"));

        Environment environment2 = new Environment();
        environment2.setId("PL2-ENV2");
        environment2.setName("Env 2 of platform 2");
        environment2.setApplications(createApplications("2-2"));

        return Arrays.asList(environment1, environment2);
    }

    private static List<Application> createApplications(String label) {
        Application application1 = new MockApplication.Builder()
                .name("app1_" + label)
                .status(ApplicationStatus.RUNNING)
                .host("host1")
                .applicationType("applicationType1")
                .port(7000)
                .componentName("component-1")
                .url("http://host1:7000/app/login")
                .version("1.12_Q20-SNAPSHOT")
                .processMemory(ThreadLocalRandom.current().nextDouble(512, 16384)).build();

        Application application2 = new MockApplication.Builder()
                .name("app2_" + label)
                .status(ApplicationStatus.RUNNING)
                .host("host2")
                .applicationType("applicationType2")
                .port(7001)
                .componentName("component-2-" + label)
                .url("http://host1:7001/app/login")
                .version("1.12_E20")
                .processMemory(ThreadLocalRandom.current().nextDouble(512, 16384)).build();

        return Arrays.asList(application1, application2);
    }

}
