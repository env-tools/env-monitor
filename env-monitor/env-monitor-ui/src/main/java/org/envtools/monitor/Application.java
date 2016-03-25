package org.envtools.monitor;

/**
 * Created: 9/19/15 11:51 PM
 *
 * @author Yury Yakovlev
 */

import org.envtools.monitor.ui.app.config.QueryLibFillerImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ImportResource;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
//@EnableScheduling //for embedded applications module: move when becomes standalone
@ImportResource("classpath:embedded-modules.xml")
public class Application {

    public static void main(String[] args) {
        if (System.getProperty("spring.profiles.active") == null) {
            //Enable default implementation
            System.setProperty("spring.profiles.active", "mock");
        }

        ApplicationContext context = SpringApplication.run(Application.class, args);
        QueryLibFillerImpl filler = (QueryLibFillerImpl) context.getBean("queryLibFillerImpl");
        filler.createDb();
    }
}
