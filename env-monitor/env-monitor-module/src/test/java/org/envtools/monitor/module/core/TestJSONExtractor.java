package org.envtools.monitor.module.core;

import org.apache.log4j.Logger;
import org.envtools.monitor.module.core.selection.Extractor;
import org.envtools.monitor.module.core.selection.JSONExtractor;
import org.envtools.monitor.module.core.selection.SimplePathSelector;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Created: 12/5/15 3:45 AM
 *
 * @author Yury Yakovlev
 */
public class TestJSONExtractor {

    private static final Logger LOGGER = Logger.getLogger(TestJSONExtractor.class);

    private Extractor<String, SimplePathSelector> extractor = new JSONExtractor();

    @Test()
    public void testRootProperty() throws Exception {
        String json =  extractor.extract("{\"root\" : \"abcdef\"}", SimplePathSelector.of("root"));
        LOGGER.info("TestJSONExtractor.testRootProperty - json = " + json);

        Assert.assertEquals("abcdef", json);
    }

    @Test()
    public void testCollectionItemById() throws Exception {
        String json =  extractor.extract("{\"items\" : [ {\"id\":\"1\"}, {\"id\":\"2\"}] }", SimplePathSelector.of("items.1"));
        LOGGER.info("TestJSONExtractor.testCollectionItemById - json = " + json);
        //Does not work now

        //TODO check and fix

        //Assert.assertEquals("...", json);
    }

}
