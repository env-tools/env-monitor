package org.envtools.monitor.module.querylibrary.services.impl.updates;

import org.apache.log4j.Logger;
import org.envtools.monitor.model.querylibrary.updates.UpdateTrigger;
import org.envtools.monitor.module.querylibrary.services.TreeUpdateService;
import org.envtools.monitor.module.querylibrary.services.TreeUpdateTriggerService;

/**
 * Created by jesa on 14.04.2016.
 */

public class TreeUpdateTask implements Runnable {
    private static final Logger LOGGER = Logger.getLogger(TreeUpdateTask.class);

    private TreeUpdateTriggerService treeUpdateTriggerService;

    private TreeUpdateService treeUpdateService;

    public TreeUpdateTask(TreeUpdateTriggerService treeUpdateTriggerService, TreeUpdateService treeUpdateService) {
        this.treeUpdateService = treeUpdateService;
        this.treeUpdateTriggerService = treeUpdateTriggerService;
    }

    @Override
    public void run() {
        while (true) {
            try {
                UpdateTrigger updateTrigger = treeUpdateTriggerService.waitForUpdateTrigger();
            } catch (InterruptedException e) {
                e.printStackTrace();
                LOGGER.error("TreeUpdateTask interrupted exception");
                break;
            }
            treeUpdateService.sendTreeUpdate();
        }
    }
}
