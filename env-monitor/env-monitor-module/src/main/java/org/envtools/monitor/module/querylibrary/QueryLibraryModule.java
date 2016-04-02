package org.envtools.monitor.module.querylibrary;

import org.apache.log4j.Logger;
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
import org.envtools.monitor.module.AbstractPluggableModule;
import org.envtools.monitor.module.ModuleConstants;
import org.envtools.monitor.module.querylibrary.dao.CategoryDao;
import org.envtools.monitor.module.querylibrary.services.QueryExecutionService;
import org.envtools.monitor.module.querylibrary.viewmapper.CategoryViewMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.messaging.SubscribableChannel;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;


import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
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

    @PersistenceContext
    protected EntityManager entityManager;

    @Autowired
    @Qualifier("transactionManager")
    protected PlatformTransactionManager transactionManager;


    public void init() {
        super.init();
        LOGGER.info("Initializing QueryLibFillerInvoke, using entityManager : " + entityManager);

        /*
        При инициализации Query Library Module необходимо выполнить следующее:
        Загрузить все корневые hibernate категории при помощи category dao - готово
        Построить Map корневых категорий: Map<String, List<Category>>, где ключ - это owner
        (или строка "_PUBLIC_" если owner=null) - готово
        Передать Map<String, List<Category>> в интерфейс CategoryViewMapper и получить Map<String, List<CategoryView>> - готово.
        (Map<String, List<CategoryView>> - not impl)
        */

        TransactionTemplate transactionTemplate = new TransactionTemplate(transactionManager);
        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus status) {
                //PUT YOUR CALL TO SERVICE HERE
                Map<String, List<Category>> treeMap = new HashMap<>();
                List<Category> listCategories = null;

                listCategories = categoryDao.getRootCategories();

                for (int i = 0; i < listCategories.size(); i++) {
                    List<Category> list = null;
                    if (!treeMap.containsKey(listCategories.get(i).getOwner())) {
                        String owner = listCategories.get(i).getOwner();
                        //Если owner нет в map, то добавляем owner и пустой лист.
                        if (owner == null) {
                            owner = ModuleConstants.OWNER_NULL;
                        }
                        treeMap.put(owner, list);
                        //добавим в пустой лист category
                        list.add(listCategories.get(i));
                        //добавим в HashMap, с ключом listCategories.get(i).getOwner() list c категорией,
                        // которую мы сохранили в list
                        treeMap.put(owner, list);
                    }
                }

                for (Map.Entry<String, List<Category>> tree : treeMap.entrySet()) {

                    LOGGER.info("\n KEY " + tree.getKey() + "\n");
                    for (int i = 0; i < tree.getValue().size(); i++) {
                        LOGGER.info("\n VALUE " + tree.getValue().get(i) + "\n");
                    }

                }
 /*
  Передать Map<String, Category> в интерфейс CategoryViewMapper и получить Map<String, CategoryView>
 (это точка интеграции с кодом Максима, до момента интеграции код может быть закомментирован)
Передать Map<String, CategoryView> в интерфейс CategoryViewMapper и получить Map<String, String>
(это точка интеграции с кодом Максима, до момента интеграции код может быть закомментирован)
  */
                //  Map<String,String> jsonMap=categoryViewMapper
                //          .mapCategoriesByOwnerToString(categoryViewMapper.mapCategoriesByOwnerToString(treeMap));

                //пока нет реализации

                Map<String, String> jsonMap = new HashMap<String, String>() {{
                    put("owner", "{\n" +
                            "  \"tree\": [\n" +
                            "    {\n" +
                            "      \"title\": \"private\",\n" +
                            "      \"categories\": [\n" +
                            "        {\n" +
                            "          \"id\": 2,\n" +
                            "          \"title\": \"First private category\",\n" +
                            "          \"queries\": [\n" +
                            "            {\n" +
                            "              \"id\": 1,\n" +
                            "              \"title\": \"Query 1\"\n" +
                            "            },\n" +
                            "            {\n" +
                            "              \"id\": 2,\n" +
                            "              \"title\": \"Query 2\"\n" +
                            "            },\n" +
                            "            {\n" +
                            "              \"id\": 3,\n" +
                            "              \"title\": \"Query 3\"\n" +
                            "            }\n" +
                            "          ],\n" +
                            "          \"categories\": []\n" +
                            "        },\n" +
                            "        {\n" +
                            "          \"id\": 3,\n" +
                            "          \"title\": \"Second private category\",\n" +
                            "          \"queries\": [],\n" +
                            "          \"categories\": [\n" +
                            "            {\n" +
                            "              \"id\": 4,\n" +
                            "              \"title\": \"Third private category\",\n" +
                            "              \"queries\": [],\n" +
                            "              \"categories\": []\n" +
                            "            }\n" +
                            "          ]\n" +
                            "        }\n" +
                            "      ]\n" +
                            "    },\n" +
                            "    {\n" +
                            "      \"title\": \"public\",\n" +
                            "      \"queries\": [],\n" +
                            "      \"categories\": [\n" +
                            "        {\n" +
                            "          \"id\": 5,\n" +
                            "          \"title\": \"First public category\",\n" +
                            "          \"categories\": [{\n" +
                            "            \"id\": 6,\n" +
                            "            \"title\": \"Second private category\",\n" +
                            "            \"queries\": [\n" +
                            "              {\n" +
                            "                \"id\": 1,\n" +
                            "                \"title\": \"Query 1\"\n" +
                            "              },\n" +
                            "              {\n" +
                            "                \"id\": 2,\n" +
                            "                \"title\": \"Query 2\"\n" +
                            "              },\n" +
                            "              {\n" +
                            "                \"id\": 3,\n" +
                            "                \"title\": \"Query 3\"\n" +
                            "              }\n" +
                            "            ],\n" +
                            "            \"categories\": []\n" +
                            "          }]\n" +
                            "        }\n" +
                            "      ]\n" +
                            "    }\n" +
                            "  ]\n" +
                            "}");
                }};

                /*Построить ResponseMessage, используя для payload конструкцию payload(MapContent.of(jsonMap))
Установить нужный тип ResponseMessage
Отправить сообщение в core module*/

                sendMessageToCore(ResponseMessage
                        .builder()
                        .payload(MapContent.of(jsonMap))
                        .type(ResponseType.CATEGORY_TREE_DATA)
                        //  .targetModuleId(ModuleConstants.QUERY_LIBRARY_MODULE_ID)
                        .build());

            }
        });
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
