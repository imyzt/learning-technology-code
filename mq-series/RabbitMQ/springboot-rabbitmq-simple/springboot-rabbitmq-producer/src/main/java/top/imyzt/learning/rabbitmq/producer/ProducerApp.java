package top.imyzt.learning.rabbitmq.producer;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author imyzt
 * @date 2019/5/22
 * @description Order
 */
@SpringBootApplication(scanBasePackages = "top.imyzt.learning.rabbitmq")
@MapperScan("top.imyzt.learning.rabbitmq.producer.mapper")
@EnableScheduling
public class ProducerApp {
    public static void main(String[] args) {
        SpringApplication.run(ProducerApp.class, args);
    }
}
