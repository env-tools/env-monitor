package org.envtools.monitor.provider.applications.configurable;

import org.apache.log4j.Logger;
import org.envtools.monitor.common.ssh.SshBatch;
import org.envtools.monitor.common.ssh.SshHelperService;
import org.envtools.monitor.model.applications.ApplicationStatus;
import org.envtools.monitor.model.applications.ApplicationsData;
import org.envtools.monitor.model.applications.ApplicationsModuleProvider;
import org.envtools.monitor.model.applications.Platform;
import org.envtools.monitor.model.applications.update.UpdateNotificationHandler;
import org.envtools.monitor.provider.applications.configurable.mapper.ConfigurableModelMapper;
import org.envtools.monitor.provider.applications.configurable.model.*;
import org.envtools.monitor.provider.applications.remote.RemoteMetricsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
    private SshHelperService sshHelperService;

    @Value("${configuration.dataPath}")
    String dataPath;

    @Override
    public void initialize(UpdateNotificationHandler handler) {
        this.handler = handler;

        LOGGER.info("ConfigurableApplicationsModuleProvider.initialize - populating data model...");
        updateConfigurableApplicationsModel();
    }

    @Override
    public ApplicationsData getApplicationsData() {
        return data;
    }

    @Scheduled(initialDelayString = "${configurable.provider.refresh.initial.delay.ms}",
            fixedDelayString = "${configurable.provider.refresh.fixed.delay.ms}")
    private void updateConfigurableApplicationsModel() {
        synchronized (data) {
            data.setPlatforms(loadPlatforms());
        }

        if (handler != null) {
            handler.sendUpdateNotification();
        } else {
            LOGGER.warn("ConfigurableApplicationsModuleProvider.updateConfigurableApplicationsModel - handler not initialized!");
        }
    }

    private void updateApplicationsStatus(List<PlatformXml> platforms) {

        SshBatch sshBatch = new SshBatch(sshHelperService);

        for (PlatformXml platform : platforms) {
            for (EnvironmentXml environment : platform.getEnvironments()) {
                for (VersionedApplicationXml application : environment.getApplications()) {
                    updateStatus(application, sshBatch);
                }
            }
        }

        sshBatch.execute();
    }

    private void updateStatus(VersionedApplicationXml application, SshBatch sshBatch) {
        String host = application.getHost();

        if (host != null && application.getMetadata() != null) {

            //Optional<ApplicationStatus> processStatus = Optional.empty();
            Optional<Double> processMemoryInMb = Optional.empty();
            Optional<String> version = Optional.empty();

            if (application.getMetadata().getApplicationLookupXml() instanceof TagBasedProcessLookupXml)   {
                TagBasedProcessLookupXml tagBased = (TagBasedProcessLookupXml) application.getMetadata().getApplicationLookupXml();

                //old approach
                //processStatus = remoteMetricsService.getProcessStatus(application, tagBased);

                //ssh-based approach
                remoteMetricsService.getProcessStatusUsingSshBatch(sshBatch,
                        status -> application.setStatus(status.orElse(ApplicationStatus.UNKNOWN)),
                        application,
                        tagBased
                        );

                processMemoryInMb = remoteMetricsService.getProcessMemoryInMb(application, tagBased);
            }

            //application.setStatus(processStatus.orElse(ApplicationStatus.UNKNOWN));
            application.setProcessMemory(processMemoryInMb.orElse(null));

            if (application.getMetadata().getVersionLookup() instanceof LinkBasedVersionLookupXml) {
                LinkBasedVersionLookupXml linkBased = (LinkBasedVersionLookupXml)application.getMetadata().getVersionLookup();
                version = remoteMetricsService.getApplicationVersion(application, linkBased);
            }

            application.setVersion(version.orElse(null));

        }

        if (application.getHostees() != null) {
            for (VersionedApplicationXml hostee : application.getHostees()) {
                updateStatus(hostee, sshBatch);
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
