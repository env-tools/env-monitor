package org.envtools.monitor;

/**
 * Created: 9/19/15 11:51 PM
 *
 * @author Yury Yakovlev
 */
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
//@EnableScheduling //for embedded applications module: move when becomes standalone
@ImportResource("classpath:embedded-modules.xml")
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
