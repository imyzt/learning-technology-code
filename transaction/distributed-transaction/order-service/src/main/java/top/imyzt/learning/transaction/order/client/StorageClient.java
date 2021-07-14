package top.imyzt.learning.transaction.order.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * @author imyzt
 * @date 2020/09/26
 * @description 库存微服务客户端
 */
@FeignClient("storage-service")
public interface StorageClient {

    /**
     * 扣库存
     * @param code 商品编码
     * @param count 商品数量
     */
    @PostMapping("/storage/{code}/{count}")
    void deduct(@PathVariable("code") String code, @PathVariable("count") Integer count);
}
