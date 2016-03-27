package org.envtools.monitor.module.querylibrary;

import org.apache.log4j.Logger;
import org.envtools.monitor.common.serialization.Serializer;
import org.envtools.monitor.model.messaging.RequestMessage;
import org.envtools.monitor.model.messaging.RequestPayload;
import org.envtools.monitor.model.messaging.ResponseMessage;
import org.envtools.monitor.model.querylibrary.execution.*;
import org.envtools.monitor.model.querylibrary.execution.view.QueryExecutionResultView;
import org.envtools.monitor.model.querylibrary.execution.view.QueryExecutionResultViewMapper;
import org.envtools.monitor.model.querylibrary.provider.QueryLibraryAuthProvider;
import org.envtools.monitor.module.AbstractPluggableModule;
import org.envtools.monitor.module.ModuleConstants;
import org.envtools.monitor.module.querylibrary.services.QueryExecutionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.SubscribableChannel;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * Created: 12.03.16 19:13
 *
 * @author Yury Yakovlev
 */
public class QueryLibraryModule extends AbstractPluggableModule {

    private static final Logger LOGGER = Logger.getLogger(QueryLibraryModule.class);

    public static final Map<String, Class<?>> PAYLOAD_TYPES = new HashMap<String, Class<?>>() {
        {
            put("execute", QueryExecutionRequest.class);
            put("executeNext", QueryExecutionNextResultRequest.class);
            put("cancel", QueryExecutionCancelRequest.class);
        }
    };

    @Autowired
    QueryExecutionService queryExecutionService;

    @Autowired
    QueryExecutionResultViewMapper mapper;

    @Autowired
    Serializer serializer;

    /**
     * This is incoming channel for QUERY_LIBRARY module
     */
    @Resource(name = "querylibrary.channel")
    SubscribableChannel queryLibraryModuleChannel;

    //TODO Auth is cross-module functionality
    @Resource(name = "querylibrary.provider")
    QueryLibraryAuthProvider queryLibraryAuthProvider;


    @Override
    protected <T> void processPayload(T payload, RequestMessage requestMessage) {
        if (payload instanceof QueryExecutionRequest) {
            processExecutionRequest((QueryExecutionRequest)payload, requestMessage);
        } else if (payload instanceof QueryExecutionNextResultRequest) {
            processExecutionNextResultRequest((QueryExecutionNextResultRequest) payload, requestMessage);
        }
    }

    private void processExecutionRequest(QueryExecutionRequest queryExecutionRequest, RequestMessage requestMessage) {
        try {
            queryExecutionService.submitForExecution(queryExecutionRequest,
                    new QueryExecutionListener() {
                        @Override
                        public void onQueryCompleted(QueryExecutionResult queryResult) {
                            sendResultMessage(queryResult, requestMessage);
                        }

                        @Override
                        public void onQueryError(Throwable t) {
                            //What if duplicates here?
                            sendResultMessage(mapper.errorResult(t), requestMessage);
                        }
                    });
        } catch (QueryExecutionException e) {
            //What if duplicates here?
            LOGGER.error("Query execution error", e);
            sendResultMessage(mapper.errorResult(e), requestMessage);
        }
    }

    private void sendResultMessage(QueryExecutionResultView resultView, RequestMessage requestMessage) {
        String resultViewAsJson = serializer.serialize(resultView);

        sendMessageToCore(ResponseMessage
                .builder()
                .requestMetaData(requestMessage)
                .payload(resultViewAsJson)
                .build());
    }

    private void sendResultMessage(QueryExecutionResult queryResult, RequestMessage requestMessage) {
        QueryExecutionResultView resultView = mapper.map(queryResult);
        sendResultMessage(resultView, requestMessage);
    }

    private void processExecutionNextResultRequest(QueryExecutionNextResultRequest queryExecutionNextResultRequest,
                                                   RequestMessage requestMessage) {

    }

    @Override
    protected SubscribableChannel getModuleChannel() {
        return queryLibraryModuleChannel;
    }

    @Override
    protected Map<String, Class<?>> getPayloadTypes() {
        return PAYLOAD_TYPES;
    }

    @Override
    public String getIdentifier() {
        return ModuleConstants.QUERY_LIBRARY_MODULE_ID;
    }

}
