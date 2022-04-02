package top.imyzt.learning.mdc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author imyzt
 */
@SpringBootApplication
@EnableScheduling
public class MdcDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(MdcDemoApplication.class, args);
    }

}
