package org.envtools.monitor.ui.app.config;

import org.apache.log4j.Logger;
import org.envtools.monitor.module.querylibrary.services.filler.QueryLibDbFiller;
import org.springframework.beans.factory.annotation.Autowired;
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
public class QueryLibFillerImpl {

    private static final Logger LOGGER = Logger.getLogger(QueryLibFillerImpl.class);

    @PersistenceContext
    protected EntityManager em;

    @PostConstruct
    public void init() {
        LOGGER.info("Initializing QueryLibFillerImpl, using entityManager : " + em);
    }

    @Transactional
    public void createDb() {
        QueryLibDbFiller.fillDatabase(em, false);
    }
}
