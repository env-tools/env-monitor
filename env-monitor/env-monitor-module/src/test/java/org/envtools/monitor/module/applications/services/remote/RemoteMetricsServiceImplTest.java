package org.envtools.monitor.module.applications.services.remote;

import junit.framework.Assert;
import org.junit.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created: 11.07.16 4:31
 *
 * @author Yury Yakovlev
 */
public class RemoteMetricsServiceImplTest {

    @Test
    public void testRegexMatching() {
        String regex = "/opt1/(.*)/";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher("/opt1/123-345-SNAPSHOT/");
        Assert.assertTrue(matcher.matches());
        String version = matcher.group(1);
        Assert.assertEquals("123-345-SNAPSHOT", version);
    }
}
