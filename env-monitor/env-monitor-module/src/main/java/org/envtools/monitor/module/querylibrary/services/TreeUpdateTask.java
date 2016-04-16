package org.envtools.monitor.module.querylibrary.services;

import org.envtools.monitor.model.querylibrary.updates.UpdateTrigger;

/**
 * Created by jesa on 14.04.2016.
 */

public class TreeUpdateTask implements Runnable {
    private TreeUpdateTriggerService treeUpdateTriggerService;

    private TreeUpdateService treeUpdateService;

    public TreeUpdateTask(TreeUpdateTriggerService treeUpdateTriggerService, TreeUpdateService treeUpdateService) {
        this.treeUpdateService = treeUpdateService;
        this.treeUpdateTriggerService = treeUpdateTriggerService;
    }

    @Override
    public void run() {
        boolean flag = true;
        while (flag) {
            try {
                UpdateTrigger updateTrigger = treeUpdateTriggerService.waitForUpdateTrigger();
            } catch (InterruptedException e) {
                e.printStackTrace();
                flag = false;
            }
            treeUpdateService.sendTreeUpdate();
        }
    }
}
