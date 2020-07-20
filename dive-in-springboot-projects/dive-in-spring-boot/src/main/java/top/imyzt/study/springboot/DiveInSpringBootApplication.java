package top.imyzt.study.springboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

/**
 * @author imyzt
 */
@SpringBootApplication
@ServletComponentScan(basePackages = "top.imyzt.study.springboot.web.servlet")
public class DiveInSpringBootApplication {

    public static void main(String[] args) {
        SpringApplication.run(DiveInSpringBootApplication.class, args);
    }

}
