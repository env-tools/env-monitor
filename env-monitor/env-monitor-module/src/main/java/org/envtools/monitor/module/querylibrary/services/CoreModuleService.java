package org.envtools.monitor.module.querylibrary.services;

import org.envtools.monitor.model.messaging.ResponseMessage;

/**
 * Created by jesa on 14.04.2016.
 */
public interface CoreModuleService {

    void sendToCore(ResponseMessage responseMessage);
}
