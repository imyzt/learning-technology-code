package top.imyzt.study.kafka.common.consumer;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@SpringBootTest
@RunWith(SpringRunner.class)
public class KafkaConsumerTest {

    @Autowired
    private KafkaConsumer kafkaConsumer;

    @Test
    public void listener() {

    }
}