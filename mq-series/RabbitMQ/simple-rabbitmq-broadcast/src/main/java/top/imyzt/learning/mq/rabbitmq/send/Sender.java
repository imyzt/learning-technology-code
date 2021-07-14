package top.imyzt.learning.mq.rabbitmq.send;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

import static top.imyzt.learning.mq.rabbitmq.receiver.Receiver.ROUTING_KEY;
import static top.imyzt.learning.mq.rabbitmq.receiver.Receiver.TEST_EXCHANGE;

/**
 * @author imyzt
 * @date 2021/01/28
 * @description 发送者
 */
@RestController
@RequestMapping
public class Sender {

    @Resource
    private AmqpTemplate amqpTemplate;

    @PostMapping("send")
    public String sender(String msg) {

        amqpTemplate.convertAndSend(TEST_EXCHANGE, ROUTING_KEY, msg);
        return msg;
    }
}