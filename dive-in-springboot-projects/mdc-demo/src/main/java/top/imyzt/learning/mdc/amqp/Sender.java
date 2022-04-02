package top.imyzt.learning.mdc.amqp;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author imyzt
 * @date 2021/08/14
 * @description 消息发送方
 */
@RestController
@Slf4j
public class Sender {

    @Resource
    private RabbitTemplate rabbitTemplate;

    @GetMapping("amqp-demo")
    public void amqpDemo() {

        MDC.put("testMdcId", "xxx");

        rabbitTemplate.convertAndSend("test-routingKey", "haha");

        log.info("发送前是否有MDC信息?");
    }
}