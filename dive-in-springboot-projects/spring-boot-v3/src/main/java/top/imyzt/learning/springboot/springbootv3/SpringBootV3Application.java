package top.imyzt.learning.springboot.springbootv3;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author imyzt
 */
@SpringBootApplication
@RestController
@RequestMapping
public class SpringBootV3Application {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootV3Application.class, args);
    }

    @GetMapping("index")
    public String index() {
        return "Hello SpringBoot 3.0";
    }
}
