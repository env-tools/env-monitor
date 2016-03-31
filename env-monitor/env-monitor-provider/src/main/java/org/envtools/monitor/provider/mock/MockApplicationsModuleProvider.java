package org.envtools.monitor.provider.mock;

import org.apache.log4j.Logger;
import org.envtools.monitor.model.applications.ApplicationsData;
import org.envtools.monitor.model.applications.ApplicationsModuleProvider;
import org.envtools.monitor.model.applications.update.UpdateNotificationHandler;
import org.envtools.monitor.model.querylibrary.db.Category;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.transaction.annotation.Transactional;


import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * Created: 10/31/15 1:49 AM
 *
 * @author Yury Yakovlev
 */
//@Component
//@Profile("mock")
//@Repository

public class MockApplicationsModuleProvider implements ApplicationsModuleProvider {

    private static final Logger LOGGER = Logger.getLogger(MockApplicationsModuleProvider.class);

    private ApplicationsData data = new ApplicationsData();

    private UpdateNotificationHandler handler;
    @PersistenceContext
    protected EntityManager em;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private MemoryDataProvider memoryDataProvider;

    @Autowired
    @Qualifier("transactionManager")
    protected PlatformTransactionManager transactionManager;

    @Override
    public void initialize(UpdateNotificationHandler handler) {
        LOGGER.info("MockApplicationsModuleProvider.initialize - populating data model...");
        this.handler = handler;

        //При инициализации Query Library модуля вычитывается из
        // базы список корневых категорий (объекты Category)

        TransactionTemplate transactionTemplate = new TransactionTemplate(transactionManager);
        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus status) {
                entityManager.createQuery("SELECT parentCategory FROM Category");
            }
        });
        updateFreeMemory();
        updateMockApplicationsModel();

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
                LOGGER.warn("MockApplicationsModuleProvider.updateFreeMemory - handler not initialized!");

            }
        }
    }

    @Scheduled(initialDelay = 1000, fixedDelay = 10000)
    protected void updateMockApplicationsModel() {
        synchronized (data) {
            data.setPlatforms(MockApplicationsDataCreator.createPlatforms());
        }
        if (handler != null) {
            handler.sendUpdateNotification();
        } else {
            LOGGER.warn("MockApplicationsModuleProvider.updateMockApplicationsModel - handler not initialized!");
        }
    }

    @Override
    public ApplicationsData getApplicationsData() {
        return data;
    }

}
