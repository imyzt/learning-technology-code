package top.imyzt.leaming.rabbitmq.delayqueue;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * RabbitMQ 延迟消息队列
 * @author imyzt
 */
@SpringBootApplication
public class DelayQueueSimpleApplication {

    public static void main(String[] args) {
        SpringApplication.run(DelayQueueSimpleApplication.class, args);
    }

}
