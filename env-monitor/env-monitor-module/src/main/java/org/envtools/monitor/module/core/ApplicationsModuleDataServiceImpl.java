package org.envtools.monitor.module.core;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created: 11/28/15 3:27 AM
 *
 * @author Yury Yakovlev
 */
public class ApplicationsModuleDataServiceImpl implements ApplicationsModuleDataService{

    private String serializedApplicationsData;

    @Override
    public String extractSerializedPartBySelector(String selector) {
        //TODO extract by selector
        return serializedApplicationsData;
    }

    @Override
    public void store(String serializedApplicationsData) {

        Lock lock = new ReentrantLock();
        try {
            lock.lock();
            this.serializedApplicationsData = serializedApplicationsData;
        } finally {
            lock.unlock();
        }
    }
}
