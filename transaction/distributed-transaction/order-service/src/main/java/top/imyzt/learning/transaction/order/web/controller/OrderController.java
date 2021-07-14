package top.imyzt.learning.transaction.order.web.controller;


import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.imyzt.learning.transaction.order.pojo.entity.Order;
import top.imyzt.learning.transaction.order.service.OrderService;

import javax.annotation.Resource;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author imyzt
 * @date 2020-09-26
 */
@RestController
@RequestMapping("/order")
public class OrderController {

    @Resource
    private OrderService orderService;

    @PostMapping("create")
    public Integer createOrder(@RequestBody Order order) {
        return orderService.createOrder(order);
    }

}
