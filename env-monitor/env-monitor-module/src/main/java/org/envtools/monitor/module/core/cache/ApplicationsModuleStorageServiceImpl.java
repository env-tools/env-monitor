package org.envtools.monitor.module.core.cache;

import org.apache.log4j.Logger;
import org.envtools.monitor.module.core.selection.Extractor;
import org.envtools.monitor.module.core.selection.SimplePathSelector;
import org.envtools.monitor.module.core.selection.exception.IllegalSelectorException;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created: 11/28/15 3:27 AM
 *
 * @author Yury Yakovlev
 */
public class ApplicationsModuleStorageServiceImpl implements ApplicationsModuleStorageService {

    private static final Logger LOGGER = Logger.getLogger(ApplicationsModuleStorageServiceImpl.class);

    private String serializedApplicationsData;

    @Autowired
    private Extractor<String, SimplePathSelector> extractor;

    @Override
    public String extractPartBySelector(String selector) {
        try {
            SimplePathSelector simplePathSelector = SimplePathSelector.of(selector);
            return extractor.extract(serializedApplicationsData, simplePathSelector);
        } catch (IllegalSelectorException e) {
            LOGGER.error("ApplicationsModuleStorageServiceImpl.extractPartBySelector - invalid selector, returning empty result: " +
            selector, e);
            return extractor.emptyExtractionResult();
        }
    }

    @Override
    public void storeFull(String data) {

        Lock lock = new ReentrantLock();
        try {
            lock.lock();
            this.serializedApplicationsData = data;
        } finally {
            lock.unlock();
        }
    }

    public void setExtractor(Extractor<String, SimplePathSelector> extractor) {
        this.extractor = extractor;
    }
}
