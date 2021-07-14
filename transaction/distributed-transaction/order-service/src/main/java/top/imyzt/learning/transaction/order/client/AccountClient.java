package top.imyzt.learning.transaction.order.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * @author imyzt
 * @date 2020/09/26
 * @description 账号微服务客户端
 */
@FeignClient("account-service")
public interface AccountClient {

    /**
     * 扣余额
     * @param userId 用户标识
     * @param money 金额
     */
    @PostMapping("/account/{userId}/{money}")
    void deduct(@PathVariable String userId, @PathVariable Integer money);

}