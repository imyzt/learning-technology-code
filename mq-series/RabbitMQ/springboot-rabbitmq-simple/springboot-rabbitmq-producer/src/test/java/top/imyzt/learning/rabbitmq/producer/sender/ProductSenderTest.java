package top.imyzt.learning.rabbitmq.producer.sender;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import top.imyzt.learning.rabbitmq.common.entity.Order;

import javax.annotation.Resource;
import java.util.UUID;
@SpringBootTest
@RunWith(SpringRunner.class)
public class ProductSenderTest {

    @Resource
    private ProductSender productSender;

    @Test
    public void sendOrder() {
        Order order = new Order();
        order.setId("20190523000000001");
        order.setName("abcc");
        order.setMessageId(System.currentTimeMillis() + "$" + UUID.randomUUID().toString());
        productSender.send(order);
    }
}