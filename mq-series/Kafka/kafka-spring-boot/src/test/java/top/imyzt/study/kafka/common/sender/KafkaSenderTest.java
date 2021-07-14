package top.imyzt.study.kafka.common.sender;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import top.imyzt.study.kafka.entity.Message;

import java.time.LocalDateTime;

import static org.junit.Assert.*;

@SpringBootTest
@RunWith(SpringRunner.class)
public class KafkaSenderTest {

    @Autowired
    private KafkaSender kafkaSender;

    @Test
    public void send() {
        Message<String> message = new Message<>();
        message.setId(123);
        message.setContent("test");
        message.setSendTime(LocalDateTime.now());

        kafkaSender.send(message);
    }
}