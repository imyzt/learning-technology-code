package top.imyzt.learning.rabbitmq.producer.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.imyzt.learning.rabbitmq.common.entity.Order;
import top.imyzt.learning.rabbitmq.producer.service.OrderService;

import javax.annotation.Resource;

/**
 * @author imyzt
 * @date 2019/5/23
 * @description OrderController
 */
@RestController
@RequestMapping("order")
public class OrderController {

    @Resource
    private OrderService orderService;

    @PostMapping
    public void createOrder(Order order) {
        orderService.createOrder(order);
    }


    @GetMapping("list")
    public Object list() {
        return orderService.list();
    }
}
