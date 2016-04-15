package org.envtools.monitor.module.querylibrary.services;

import org.envtools.monitor.model.querylibrary.updates.UpdateTrigger;

/**
 * Created by jesa on 12.04.2016.
 */
public interface TreeUpdateTriggerService {
    void triggerUpdate() throws InterruptedException;
    UpdateTrigger waitForUpdateTrigger() throws InterruptedException;
}
