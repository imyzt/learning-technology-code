package top.imyzt.learning.rabbitmq.producer.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import top.imyzt.learning.rabbitmq.common.entity.Order;

import javax.annotation.Resource;
import java.util.UUID;

@SpringBootTest
@RunWith(SpringRunner.class)
public class OrderServiceTest {

    @Resource
    private OrderService orderService;

    @Test
    public void createOrder() {

        Order order = new Order();
        order.setId("201905230000001")
                .setName("abcdefg")
                .setMessageId(System.currentTimeMillis() + "$" + UUID.randomUUID().toString());

        orderService.createOrder(order);
    }
}