package top.imyzt.learning.netty;

import cn.hutool.core.util.RandomUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 基于jdk18
 * @author imyzt
 */
@SpringBootApplication
@Slf4j
@RestController
public class TestingNettyApp {

    public static void main(String[] args) {
        SpringApplication.run(TestingNettyApp.class, args);
    }

    @GetMapping
    public Long sayHello(String content) {

        long messageId = RandomUtil.randomLong();
        Message message = new Message(messageId, content);

        NettyClient.SEND_QUEUE.add(message);

        log.info("Send Message -> msgId={}, content={}", messageId, content);

        return messageId;
    }

}
