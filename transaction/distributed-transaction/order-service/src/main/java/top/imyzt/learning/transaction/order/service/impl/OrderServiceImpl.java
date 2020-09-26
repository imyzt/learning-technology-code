package top.imyzt.learning.transaction.order.service.impl;

import feign.FeignException;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import top.imyzt.learning.transaction.order.client.AccountClient;
import top.imyzt.learning.transaction.order.client.StorageClient;
import top.imyzt.learning.transaction.order.pojo.entity.Order;
import top.imyzt.learning.transaction.order.dao.mapper.OrderMapper;
import top.imyzt.learning.transaction.order.service.OrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author imyzt
 * @date 2020-09-26
 */
@Service
@Slf4j
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {

    @Resource
    private AccountClient accountClient;
    @Resource
    private StorageClient storageClient;

    @Override
//    @Transactional(rollbackFor = Exception.class)
    @GlobalTransactional(rollbackFor = Exception.class)
    public Integer createOrder(Order order) {

        log.info("开始下单");

        save(order);

        try {
            // 调用库存服务, 扣库存
            storageClient.deduct(order.getCommodityCode(), order.getCount());

            // 调用账户服务, 扣余额
            accountClient.deduct(order.getUserId(), order.getMoney());
        } catch (FeignException e) {
            log.error("下单失败, 原因: {}", e.contentUTF8());
            throw new RuntimeException(e.contentUTF8());
        }

        log.info("下单成功");

        return order.getId();
    }
}
