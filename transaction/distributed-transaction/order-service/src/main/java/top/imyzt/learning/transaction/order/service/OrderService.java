package top.imyzt.learning.transaction.order.service;

import top.imyzt.learning.transaction.order.pojo.entity.Order;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author imyzt
 * @date 2020-09-26
 */
public interface OrderService extends IService<Order> {

    /**
     * 下单
     * @param order 订单信息
     * @return 订单id
     */
    Integer createOrder(Order order);

}
