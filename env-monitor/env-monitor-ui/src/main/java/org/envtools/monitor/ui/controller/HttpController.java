package org.envtools.monitor.ui.controller;

import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.AbstractEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.PropertySource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Created: 9/15/16 3:32 PM
 *
 * @author Yury Yakovlev
 */
@Controller
public class HttpController {

    @Autowired
    private Environment env;

    private static final String PREFIX = "settings.";

    @RequestMapping("/settings")
    @ResponseBody
    public Map<String, Object> getSettings() {

        Map<String, Object> settings =
                StreamSupport
                        .stream(
                                ((AbstractEnvironment) env).getPropertySources().spliterator(), false)
                        .filter(s -> s instanceof MapPropertySource)
                        .flatMap(s -> ((MapPropertySource) s).getSource().entrySet().stream())
                        .filter(entry -> entry.getKey().startsWith(PREFIX))
                        .collect(Collectors.toMap(
                                entry -> entry.getKey().substring(PREFIX.length()),
                                entry -> entry.getValue(),
                                (oldVal, newVal) -> oldVal));

        //Order is important, overrides are encountered first, hence preferred
        // => (oldVal, newVal) -> oldVal)
        return settings;
    }
}
