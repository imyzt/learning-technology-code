package top.imyzt.learning.demo.rocketmq5;

import org.apache.rocketmq.client.apis.ClientConfiguration;
import org.apache.rocketmq.client.apis.ClientConfigurationBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class DemoRocketmq5Application {

    @Value("${rocketmq.endpoints}")
    private String endpoints;

    public static void main(String[] args) {
        SpringApplication.run(DemoRocketmq5Application.class, args);
    }

    @Bean(name = "rocketmqClientConfiguration")
    public ClientConfiguration rocketmqClientConfiguration() {
        ClientConfigurationBuilder builder = ClientConfiguration.newBuilder().setEndpoints(endpoints);
        return builder.build();
    }
}
