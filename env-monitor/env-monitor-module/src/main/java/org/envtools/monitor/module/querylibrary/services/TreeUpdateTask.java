package org.envtools.monitor.module.querylibrary.services;

import org.envtools.monitor.model.querylibrary.updates.UpdateTrigger;

import java.sql.SQLException;

/**
 * Created by jesa on 14.04.2016.
 */
public class TreeUpdateTask implements Runnable {
    private TreeUpdateTriggerService treeUpdateTriggerService;
    private TreeUpdateService treeUpdateService = new TreeUpdateServiceImpl();

    public TreeUpdateTriggerService getTreeUpdateTriggerService() {
        return treeUpdateTriggerService;
    }

    public void setTreeUpdateTriggerService(TreeUpdateTriggerService treeUpdateTriggerService) {
        this.treeUpdateTriggerService = treeUpdateTriggerService;
    }

    public TreeUpdateService getTreeUpdateService() {
        return treeUpdateService;
    }

    public void setTreeUpdateService(TreeUpdateService treeUpdateService) {
        this.treeUpdateService = treeUpdateService;
    }

    public TreeUpdateTask(TreeUpdateTriggerService treeUpdateTriggerService) {
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
            try {
                treeUpdateService.sendTreeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
