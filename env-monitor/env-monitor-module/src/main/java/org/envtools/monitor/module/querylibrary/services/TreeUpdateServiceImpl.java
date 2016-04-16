package org.envtools.monitor.module.querylibrary.services;

import com.google.common.collect.Lists;
import org.apache.log4j.Logger;
import org.envtools.monitor.model.messaging.ResponseMessage;
import org.envtools.monitor.model.messaging.ResponseType;
import org.envtools.monitor.model.messaging.content.MapContent;
import org.envtools.monitor.model.querylibrary.db.Category;
import org.envtools.monitor.module.ModuleConstants;
import org.envtools.monitor.module.querylibrary.dao.CategoryDao;
import org.envtools.monitor.module.querylibrary.viewmapper.CategoryViewMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jesa on 12.04.2016.
 */
@Service
public class TreeUpdateServiceImpl implements TreeUpdateService {

    private static final Logger LOGGER = Logger.getLogger(TreeUpdateServiceImpl.class);

    @Autowired
    CategoryDao categoryDao;

    @Autowired
    CategoryViewMapper categoryViewMapper;

    @Autowired
    @Qualifier("transactionManager")
    protected PlatformTransactionManager transactionManager;

    @Autowired
    CoreModuleService coreModuleService;

    @Override
    public void sendTreeUpdate() {
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

                coreModuleService.sendToCore(ResponseMessage
                        .builder()
                        .payload(MapContent.of(jsonMap))
                        .type(ResponseType.CATEGORY_TREE_DATA)
                        .targetModuleId(ModuleConstants.QUERY_LIBRARY_MODULE_ID)
                        .build());

            }
        });
    }
}
