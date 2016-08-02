package org.envtools.monitor.provider.applications.mock;

import com.jcraft.jsch.JSchException;
import org.apache.log4j.Logger;
import org.envtools.monitor.common.encrypting.EncryptionService;
import org.envtools.monitor.common.encrypting.EncryptionServiceImpl;
import org.envtools.monitor.common.ssh.*;
import org.envtools.monitor.model.applications.ApplicationStatus;
import org.envtools.monitor.model.applications.ApplicationsData;
import org.envtools.monitor.model.applications.ApplicationsModuleProvider;
import org.envtools.monitor.model.applications.Platform;
import org.envtools.monitor.model.applications.update.UpdateNotificationHandler;
import org.envtools.monitor.provider.applications.configurable.ConfigurableApplicationProvider;
import org.envtools.monitor.provider.applications.configurable.mapper.ConfigurableModelMapper;
import org.envtools.monitor.provider.applications.configurable.model.*;
import org.envtools.monitor.provider.applications.remote.RemoteMetricsService;
import org.envtools.monitor.provider.applications.remote.RemoteMetricsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.annotation.Scheduled;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

/**
 * Created by Michal Skuza on 27/07/16.
 */
public class TestApplicationsModuleProvider implements ApplicationsModuleProvider {
    private static final Logger LOGGER = Logger.getLogger(TestApplicationsModuleProvider.class);

    private UpdateNotificationHandler handler;

    private ApplicationsData data = new ApplicationsData();

    @Autowired
    private ConfigurableModelMapper configurableModelMapper;

    @Autowired
    private ConfigurableApplicationProvider configurableApplicationProvider;

    @Autowired
    private SshCredentialsService sshCredentialsService;

    @Autowired
    private SshHelperServiceFactory sshHelperServiceFactory;

    private SshHelperService sshHelperService;

    private RemoteMetricsService remoteMetricsService;

    @Autowired
    private MemoryDataProvider memoryDataProvider;

    @Autowired
    private EncryptionService encryptionService;

    @Value("${hosts.list}")
    private List<String> hosts;

    @Value("${dataPath}")
    String dataPath;

    @PostConstruct
    public void registerSshHelpers() throws IOException {
        sshHelperService = sshHelperServiceFactory.buildSshHelperService(hosts);
        sshHelperService.loginAllSshHelpers();
        remoteMetricsService = new RemoteMetricsServiceImpl(sshHelperService);

        LOGGER.info("Registered SshHelpers");
    }

    @PreDestroy
    public void logoutSshHelpers(){
        sshHelperService.logoutAllSshHelpers();
        LOGGER.info("SshHelpers logged out");
    }

    @Override
    public void initialize(UpdateNotificationHandler handler) {
        LOGGER.info("TestApplicationsModuleProvider.initialize - populating data model...");
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
                LOGGER.warn("TestApplicationsModuleProvider.updateFreeMemory - handler not initialized!");
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
            LOGGER.warn("TestApplicationsModuleProvider.updateMockApplicationsModel - handler not initialized!");
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

        if (host != null) {
            Optional<ApplicationStatus> processStatus = remoteMetricsService.getProcessStatus(application, (TagBasedProcessLookupXml) application.getMetadata().getApplicationLookupXml());
            application.setStatus(processStatus.get());

            Optional<Double> processMemoryInMb = remoteMetricsService.getProcessMemoryInMb(application, (TagBasedProcessLookupXml) application.getMetadata().getApplicationLookupXml());
            if (processMemoryInMb.isPresent()) {
                application.setProcessMemory(processMemoryInMb.get());
            } else {
                application.setProcessMemory(null);
            }
        }
    }

    private VersionedApplicationPropertiesXml loadVersionedApplicationPropertiesXml(String filePath) {
        try {
            ClassPathResource classPathResource = new ClassPathResource(filePath);
            return configurableApplicationProvider.readConfigurationFromFile(classPathResource.getFile());
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
