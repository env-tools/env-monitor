package org.envtools.monitor.module;

import org.envtools.monitor.model.messaging.RequestMessage;
import org.envtools.monitor.model.messaging.ResponseMessage;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.SubscribableChannel;
import org.springframework.messaging.support.GenericMessage;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * Created: 12.03.16 19:18
 *
 * @author Yury Yakovlev
 */
public abstract class AbstractPluggableModule implements Module {

    @Resource(name = "core.channel")
    MessageChannel coreModuleChannel;

    private MessageHandler incomingMessageHandler = (message) -> handleIncomingMessage((RequestMessage) message.getPayload());

    @PostConstruct
    public void init() {
        getModuleChannel().subscribe(incomingMessageHandler);
    }

    public abstract void handleIncomingMessage(RequestMessage requestMessage);

    public void sendMessageToCore(ResponseMessage responseMessage) {

        coreModuleChannel.send(new GenericMessage<ResponseMessage>(responseMessage));

    }

    protected abstract SubscribableChannel getModuleChannel() ;
}
