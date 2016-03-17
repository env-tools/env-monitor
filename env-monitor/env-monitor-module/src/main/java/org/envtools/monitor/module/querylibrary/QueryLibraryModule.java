package org.envtools.monitor.module.querylibrary;

import org.apache.log4j.Logger;
import org.envtools.monitor.model.messaging.RequestMessage;
import org.envtools.monitor.model.querylibrary.provider.QueryLibraryAuthProvider;
import org.envtools.monitor.module.AbstractPluggableModule;
import org.envtools.monitor.module.ModuleConstants;
import org.springframework.messaging.SubscribableChannel;

import javax.annotation.Resource;

/**
 * Created: 12.03.16 19:13
 *
 * @author Yury Yakovlev
 */
public class QueryLibraryModule extends AbstractPluggableModule {

    private static final Logger LOGGER = Logger.getLogger(QueryLibraryModule.class);

    /**
     * This is incoming channel for APPLICATIONS module
     */
    @Resource(name = "querylibrary.channel")
    SubscribableChannel queryLibraryModuleChannel;

    @Resource(name = "querylibrary.provider")
    QueryLibraryAuthProvider queryLibraryAuthProvider;

    @Override
    public void handleIncomingMessage(RequestMessage requestMessage) {
        //TODO implement
    }

    @Override
    protected SubscribableChannel getModuleChannel() {
        return queryLibraryModuleChannel;
    }

    @Override
    public String getIdentifier() {
        return ModuleConstants.QUERY_LIBRARY_MODULE_ID;
    }
}
