package org.envtools.monitor.module.querylibrary.dbadmin;

import org.apache.log4j.Logger;
import org.h2.server.web.WebServlet;
import org.springframework.boot.context.embedded.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created: 04.03.16 20:05
 *
 * @author Yury Yakovlev
 */

@Configuration
public class WebConfiguration {
    //TODO
    // If whenever the module starts in a separate JVM,
    // respective additional web configuration must be provided

    private static final Logger LOGGER = Logger.getLogger(WebConfiguration.class);

    @Bean
    ServletRegistrationBean h2servletRegistration() {
        ServletRegistrationBean registrationBean = new ServletRegistrationBean(new WebServlet());
        registrationBean.addUrlMappings("/h2-console/*");

        LOGGER.info("H2 console registered at /h2-console path");

        return registrationBean;
    }
}
