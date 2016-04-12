package org.envtools.monitor.module.core;

import org.apache.commons.io.IOUtils;
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
        String json = extractor.extract("{\"root\" : \"abcdef\"}", SimplePathSelector.of("root"));
        LOGGER.info("TestJSONExtractor.testRootProperty - json = " + json);

        Assert.assertEquals("abcdef", json);
    }

    @Test()
    public void testCollectionItemById() throws Exception {
        String json = extractor.extract(
                "{\"items\" : [ {\"id\":\"1\"}, {\"id\":\"2\"}] }",
                SimplePathSelector.of("items/1"));
        // LOGGER.info("TestJSONExtractor.testCollectionItemById - json = " + json);
        // Assert.assertEquals("[{\"id\":\"1\"}][0]", json);
    }

    @Test()
    public void testNestedProperty() throws Exception {
        String json = extractor.extract(
                "{\"item\":{\"id\":\"1\",\"name\":\"test\"} }",
                SimplePathSelector.of("item/name"));
        // LOGGER.info("TestJSONExtractor.testNestedProperty - json = " + json);
        // Assert.assertEquals("test", json);
    }

    @Test()
    public void testComplexStructure() throws Exception {
        String source = IOUtils.toString(getClass().getResourceAsStream("/applications/test-complex-structure.json"));
        String json = extractor.extract(
                source,
                SimplePathSelector.of("data/platforms/ID_MCS_PRIME/environments/ID_DEV9/applications"));
        // LOGGER.info("TestJSONExtractor.testComplexStructure - json = " + json);
        // Assert.assertEquals("test", json);
    }

}
