package org.envtools.monitor.ui.app.config;

import org.apache.log4j.Logger;
import org.envtools.monitor.module.querylibrary.services.filler.QueryLibDbFiller;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Created by sergey on 20.03.2016.
 */

@Component
@Transactional
public class QueryLibFillerInvoke {

    private static final Logger LOGGER = Logger.getLogger(QueryLibFillerInvoke.class);

    @PersistenceContext
    protected EntityManager em;

    @PostConstruct
    public void init() {
        LOGGER.info("Initializing QueryLibFillerInvoke, using entityManager : " + em);
    }

    @Transactional
    public void createDb(boolean createSampleTestHistory) {
        QueryLibDbFiller.fillDatabase(em, createSampleTestHistory);
    }
}
