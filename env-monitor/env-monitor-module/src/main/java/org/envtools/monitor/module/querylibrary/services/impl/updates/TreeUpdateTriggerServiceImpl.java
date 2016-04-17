package org.envtools.monitor.module.querylibrary.services.impl.updates;

import org.envtools.monitor.model.querylibrary.updates.UpdateTrigger;
import org.envtools.monitor.module.querylibrary.services.TreeUpdateTriggerService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by jesa on 13.04.2016.
 */
@Service
public class TreeUpdateTriggerServiceImpl implements TreeUpdateTriggerService {
    BlockingQueue<UpdateTrigger> blockingQueue = new LinkedBlockingQueue<UpdateTrigger>();

    @Override
    public void triggerUpdate() throws InterruptedException {
        blockingQueue.put(new UpdateTrigger(LocalDateTime.now()));
    }

    @Override
    public UpdateTrigger waitForUpdateTrigger() throws InterruptedException {
        UpdateTrigger updateTrigger = blockingQueue.take();
        return updateTrigger;
    }
}
