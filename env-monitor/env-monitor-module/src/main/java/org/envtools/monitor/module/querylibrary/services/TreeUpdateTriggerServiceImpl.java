package org.envtools.monitor.module.querylibrary.services;

import org.envtools.monitor.model.querylibrary.updates.UpdateTrigger;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by jesa on 13.04.2016.
 */
@Service
public class TreeUpdateTriggerServiceImpl implements TreeUpdateTriggerService {
    BlockingQueue blockingQueue = new LinkedBlockingQueue<UpdateTrigger>();

    @Override
    public void triggerUpdate() throws InterruptedException {
        blockingQueue.put(new UpdateTrigger(LocalDateTime.now()));
    }

    @Override
    public UpdateTrigger waitForUpdateTrigger() throws InterruptedException {
        UpdateTrigger updateTrigger = (UpdateTrigger) blockingQueue.take();
        return updateTrigger;
    }
}
