package org.envtools.monitor.module.querylibrary;

import com.google.common.collect.Lists;
import org.apache.log4j.Logger;
import org.apache.tomcat.jdbc.pool.DataSource;
import org.envtools.monitor.common.serialization.Serializer;
import org.envtools.monitor.model.messaging.RequestMessage;
import org.envtools.monitor.model.messaging.ResponseMessage;
import org.envtools.monitor.model.messaging.ResponseType;
import org.envtools.monitor.model.messaging.content.MapContent;
import org.envtools.monitor.model.querylibrary.db.Category;
import org.envtools.monitor.model.querylibrary.execution.*;
import org.envtools.monitor.model.querylibrary.execution.view.QueryExecutionResultView;
import org.envtools.monitor.model.querylibrary.provider.QueryLibraryAuthProvider;
import org.envtools.monitor.model.querylibrary.tree.view.CategoryView;
import org.envtools.monitor.model.updates.DataOperation;
import org.envtools.monitor.module.AbstractPluggableModule;
import org.envtools.monitor.module.ModuleConstants;
import org.envtools.monitor.module.querylibrary.dao.CategoryDao;
import org.envtools.monitor.module.querylibrary.services.QueryExecutionService;
import org.envtools.monitor.module.querylibrary.viewmapper.CategoryViewMapper;
import org.h2.tools.RunScript;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.messaging.SubscribableChannel;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;


import javax.annotation.Resource;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

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
            put("dataOperation", DataOperation.class);
        }
    };

    @Autowired
    QueryExecutionService queryExecutionService;

    @Autowired
    org.envtools.monitor.module.querylibrary.viewmapper.QueryExecutionResultViewMapper mapper;

    @Autowired
    Serializer serializer;

    private AtomicLong responseIdentifier = new AtomicLong(0);

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
            processExecutionRequest((QueryExecutionRequest) payload, requestMessage);
        } else if (payload instanceof QueryExecutionNextResultRequest) {
            processExecutionNextResultRequest((QueryExecutionNextResultRequest) payload, requestMessage);
        } else if (payload instanceof DataOperation) {
            processDataOperationRequest((DataOperation) payload, requestMessage);
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

    @Autowired
    CategoryDao categoryDao;

    @Autowired
    CategoryViewMapper categoryViewMapper;

    @Autowired
    DataSource dataSource;

    @Autowired
    @Qualifier("transactionManager")
    protected PlatformTransactionManager transactionManager;

    public void init() throws Exception {
        super.init();
        Connection connection = dataSource.getConnection();
        InputStream stream = this.getClass().getClassLoader().getResourceAsStream("sql/test_fill_c_q.sql");
        RunScript.execute(connection, new InputStreamReader(stream));

        TransactionTemplate transactionTemplate = new TransactionTemplate(transactionManager);
        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus status) {

                Map<String, List<Category>> treeMap = new HashMap<>();
                List<Category> listCategories = categoryDao.getRootCategories();

                for (Category category : listCategories) {
                    String owner = category.getOwner();
                    String ownerKey = owner == null ? ModuleConstants.OWNER_NULL : owner;

                    List<Category> ownerCategories;
                    if (!treeMap.containsKey(ownerKey)) {
                        ownerCategories = Lists.newArrayList();
                        treeMap.put(ownerKey, ownerCategories);
                    } else {
                        ownerCategories = treeMap.get(ownerKey);
                    }
                    ownerCategories.add(category);
                }

                for (Map.Entry<String, List<Category>> tree : treeMap.entrySet()) {

                    LOGGER.info("\n KEY " + tree.getKey() + "\n");
                    for (int i = 0; i < tree.getValue().size(); i++) {
                        LOGGER.info("\n VALUE #" + i + " " + tree.getValue().get(i) + "\n");
                    }

                }

                Map<String, String> jsonMap = null;
                try {
                    jsonMap = categoryViewMapper
                            .mapCategoriesByOwnerToString(categoryViewMapper.mapCategoriesByOwner(treeMap));
                } catch (IOException e) {
                    e.printStackTrace();
                }

                sendMessageToCore(ResponseMessage
                        .builder()
                        .payload(MapContent.of(jsonMap))
                        .type(ResponseType.CATEGORY_TREE_DATA)
                        .targetModuleId(ModuleConstants.QUERY_LIBRARY_MODULE_ID)
                        .build());

            }
        });
    }

    private void sendResultMessage(QueryExecutionResultView resultView, RequestMessage requestMessage) {
        String resultViewAsJson = serializer.serialize(resultView);

        sendMessageToCore(ResponseMessage
                .builder()
                .requestMetaData(requestMessage)
                .type(ResponseType.QUERY_EXECUTION_RESULT)
                .payload(resultViewAsJson)
                .build());
    }

    private void sendResultMessage(QueryExecutionResult queryResult, RequestMessage requestMessage) {
        QueryExecutionResultView resultView = mapper.map(queryResult);
        sendResultMessage(resultView, requestMessage);
    }

    private void processDataOperationRequest(DataOperation dataOperation, RequestMessage requestMessage) {
        String result = "{}";

        sendMessageToCore(ResponseMessage
                .builder()
                .requestMetaData(requestMessage)
                .type(ResponseType.DATA_OPERATION_RESULT)
                .payload(result)
                .build());
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
