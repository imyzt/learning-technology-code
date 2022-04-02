package top.imyzt.learning.mdc.amqp;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

/**
 * @author imyzt
 * @date 2021/08/14
 * @description 描述信息
 */
@Component
@Slf4j
public class Received {

    @RabbitHandler
    @RabbitListener(queues = {"test-routingKey"})
    public void handler(Message message) {

        log.info("收到消息, body={}", message);
    }
}