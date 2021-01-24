package top.imyzt.learing.productionready;

import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

//@SpringBootApplication
public class ProductionReadyApplication {

    public static void main(String[] args) {
        SpringApplicationBuilder builder = new SpringApplicationBuilder(ProductionReadyApplication.class);
        ConfigurableApplicationContext run = builder.bannerMode(Banner.Mode.OFF)
                .run(args);
    }
}
