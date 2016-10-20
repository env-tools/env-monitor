package org.envtools.monitor;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Created: 10/18/16 8:54 PM
 *
 * @author Yury Yakovlev
 */
@Configuration
public class StaticResourceConfiguration  extends WebMvcConfigurerAdapter {

    private static final String[] RESOURCE_LOCATIONS = {

            "file:env-monitor-ui/src/main/resources/static/",
            "classpath:/META-INF/resources/", "classpath:/resources/",
            "classpath:/static/", "classpath:/public/" };

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**").addResourceLocations(RESOURCE_LOCATIONS).setCachePeriod(0);
    }
}