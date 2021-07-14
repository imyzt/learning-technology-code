package top.imyzt.learning.listener.springlistener;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

/**
 * @author imyzt
 * @date 2021/01/26
 * @description SpringBootApplicationListener 监听Spring事件
 */
@SpringBootApplication
public class SpringEventListenerBootstrap {

    public static void main(String[] args) {
        new SpringApplicationBuilder(Object.class)
                .listeners(event ->
                        System.out.println("SpringBoot event -> " + event.getClass().getSimpleName()
                                + ", source -> " + event.getSource().getClass().getSimpleName()))
                .web(WebApplicationType.NONE)
                .run(args)
                .close();

        //SpringBoot event -> ApplicationStartingEvent, source -> SpringApplication
        //SpringBoot event -> ApplicationEnvironmentPreparedEvent, source -> SpringApplication
        //
        //  .   ____          _            __ _ _
        // /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
        //( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
        // \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
        //  '  |____| .__|_| |_|_| |_\__, | / / / /
        // =========|_|==============|___/=/_/_/_/
        // :: Spring Boot ::                (v2.4.2)
        //
        //SpringBoot event -> ApplicationContextInitializedEvent, source -> SpringApplication
        //SpringBoot event -> ApplicationPreparedEvent, source -> SpringApplication
        //SpringBoot event -> ContextRefreshedEvent, source -> AnnotationConfigApplicationContext
        //SpringBoot event -> ApplicationStartedEvent, source -> SpringApplication
        //SpringBoot event -> AvailabilityChangeEvent, source -> AnnotationConfigApplicationContext
        //SpringBoot event -> ApplicationReadyEvent, source -> SpringApplication
        //SpringBoot event -> AvailabilityChangeEvent, source -> AnnotationConfigApplicationContext
        //SpringBoot event -> ContextClosedEvent, source -> AnnotationConfigApplicationContext
    }
}