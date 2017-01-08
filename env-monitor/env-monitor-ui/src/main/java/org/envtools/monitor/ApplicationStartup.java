package org.envtools.monitor;

/**
 * Created: 9/19/15 11:51 PM
 *
 * @author Yury Yakovlev
 */

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;

@SpringBootApplication
//@EnableScheduling //for embedded applications module: move when becomes standalone
@ImportResource("classpath:embedded-modules.xml")
public class ApplicationStartup {

    public static void main(String[] args) {

        SpringApplication.run(ApplicationStartup.class, args);

    }
}
