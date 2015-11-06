package org.envtools.monitor.provider.mock;

import org.apache.log4j.Logger;
import org.envtools.monitor.model.applications.ApplicationsData;
import org.envtools.monitor.model.applications.ApplicationsModuleProvider;
import org.envtools.monitor.model.applications.NotificationHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * Created: 10/31/15 1:49 AM
 *
 * @author Yury Yakovlev
 */
public class MockApplicationsModuleProvider implements ApplicationsModuleProvider {

    private static final Logger LOGGER = Logger.getLogger(MockApplicationsModuleProvider.class);

    private ApplicationsData data = new ApplicationsData();

    private NotificationHandler handler;

    @Autowired
    private MemoryDataProvider memoryDataProvider;

    @Override
    public void initialize(NotificationHandler handler) {
        LOGGER.info("MockApplicationsModuleProvider.initialize - populating data model...");
        this.handler = handler;
        Long freeMemory = memoryDataProvider.getFreeMemory();

        //TODO: consider refactoring synchronization approach
        synchronized (data) {
            data.setFreeMemory(freeMemory);
        }

        handler.sendUpdateNotification();
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
            handler.sendUpdateNotification();
        }
    }

    @Override
    public ApplicationsData getApplicationsData() {
        return data;
    }

}
