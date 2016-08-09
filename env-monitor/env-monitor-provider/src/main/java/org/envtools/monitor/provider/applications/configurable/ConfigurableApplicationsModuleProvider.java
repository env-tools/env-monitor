package org.envtools.monitor.provider.applications.configurable;

import org.apache.log4j.Logger;
import org.envtools.monitor.model.applications.ApplicationStatus;
import org.envtools.monitor.model.applications.ApplicationsData;
import org.envtools.monitor.model.applications.ApplicationsModuleProvider;
import org.envtools.monitor.model.applications.Platform;
import org.envtools.monitor.model.applications.update.UpdateNotificationHandler;
import org.envtools.monitor.provider.applications.configurable.mapper.ConfigurableModelMapper;
import org.envtools.monitor.provider.applications.configurable.model.*;
import org.envtools.monitor.provider.applications.mock.MemoryDataProvider;
import org.envtools.monitor.provider.applications.remote.RemoteMetricsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.util.ResourceUtils;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

/**
 * Created by Michal Skuza on 27/07/16.
 */
public class ConfigurableApplicationsModuleProvider implements ApplicationsModuleProvider {
    private static final Logger LOGGER = Logger.getLogger(ConfigurableApplicationsModuleProvider.class);

    private UpdateNotificationHandler handler;

    private ApplicationsData data = new ApplicationsData();

    @Autowired
    private ConfigurableModelMapper configurableModelMapper;

    @Autowired
    private ConfigurationReader configurationReader;

    @Autowired
    private RemoteMetricsService remoteMetricsService;

    @Autowired
    private MemoryDataProvider memoryDataProvider;

    @Value("${configuration.dataPath}")
    String dataPath;

    @Override
    public void initialize(UpdateNotificationHandler handler) {
        LOGGER.info("ConfigurableApplicationsModuleProvider.initialize - populating data model...");
        this.handler = handler;
        updateFreeMemory();
        updateTestApplicationsModel();
    }

    @Override
    public ApplicationsData getApplicationsData() {
        return data;
    }

    @Scheduled(initialDelay = 2000, fixedDelay = 5000)
    protected void updateFreeMemory() {
        Long newFreeMemory = memoryDataProvider.getFreeMemory();
        boolean sendUpdate = false;
        if (!newFreeMemory.equals(data.getFreeMemory())) {
            synchronized (data) {
                if (newFreeMemory != data.getFreeMemory()) {
                    data.setFreeMemory(newFreeMemory);
                    sendUpdate = true;
                }
            }
        }

        if (sendUpdate) {
            if (handler != null) {
                handler.sendUpdateNotification();
            } else {
                LOGGER.warn("ConfigurableApplicationsModuleProvider.updateFreeMemory - handler not initialized!");
            }
        }
    }

    @Scheduled(initialDelay = 240000, fixedDelay = 240000)
    private void updateTestApplicationsModel() {
        synchronized (data) {
            data.setPlatforms(loadPlatforms());
        }

        if (handler != null) {
            handler.sendUpdateNotification();
        } else {
            LOGGER.warn("ConfigurableApplicationsModuleProvider.updateMockApplicationsModel - handler not initialized!");
        }
    }

    private void updateApplicationsStatus(List<PlatformXml> platforms) {
        for (PlatformXml platform : platforms) {
            for (EnvironmentXml environment : platform.getEnvironments()) {
                for (VersionedApplicationXml application : environment.getApplications()) {
                    updateStatus(application);
                }
            }
        }
    }

    private void updateStatus(VersionedApplicationXml application) {
        String host = application.getHost();

        if (host != null && application.getMetadata() != null) {

            Optional<ApplicationStatus> processStatus = Optional.empty();
            Optional<Double> processMemoryInMb = Optional.empty();
            Optional<String> version = Optional.empty();

            if (application.getMetadata().getApplicationLookupXml() instanceof TagBasedProcessLookupXml)   {
                TagBasedProcessLookupXml tagBased = (TagBasedProcessLookupXml) application.getMetadata().getApplicationLookupXml();
                processStatus = remoteMetricsService.getProcessStatus(application, tagBased);
                processMemoryInMb = remoteMetricsService.getProcessMemoryInMb(application, tagBased);
            }

            application.setStatus(processStatus.orElse(ApplicationStatus.UNKNOWN));
            application.setProcessMemory(processMemoryInMb.orElse(null));

            if (application.getMetadata().getVersionLookup() instanceof LinkBasedVersionLookupXml) {
                LinkBasedVersionLookupXml linkBased = (LinkBasedVersionLookupXml)application.getMetadata().getVersionLookup();
                version = remoteMetricsService.getApplicationVersion(application, linkBased);
            }

            application.setVersion(version.orElse(null));

        }

        if (application.getHostees() != null) {
            for (VersionedApplicationXml hostee : application.getHostees()) {
                updateStatus(hostee);
            }
        }
    }

    private VersionedApplicationPropertiesXml loadVersionedApplicationPropertiesXml(String resourcePath) {
        try {
            return configurationReader.readConfigurationFromFile(ResourceUtils.getFile(resourcePath));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private List<Platform> loadPlatforms() {
        VersionedApplicationPropertiesXml configurableAppProperties = loadVersionedApplicationPropertiesXml(dataPath);
        updateApplicationsStatus(configurableAppProperties.getPlatforms());
        ApplicationsData applicationsData = configurableModelMapper.map(configurableAppProperties);
        return applicationsData.getPlatforms();
    }
}
