package org.envtools.monitor.module.querylibrary.services;

import org.envtools.monitor.model.messaging.ResponseMessage;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by jesa on 14.04.2016.
 */
@Service
public class CoreModuleServiceImpl implements CoreModuleService {
    @Resource(name = "core.channel")
    MessageChannel coreModuleChannel;

    public MessageChannel getCoreModuleChannel() {
        return coreModuleChannel;
    }

    public void setCoreModuleChannel(MessageChannel coreModuleChannel) {
        this.coreModuleChannel = coreModuleChannel;
    }

    @Override
    public void sendToCore(ResponseMessage responseMessage) {
        getCoreModuleChannel().send(new GenericMessage<ResponseMessage>(responseMessage));
    }
}
