package top.imyzt.learning.feign.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.GetMapping;
import top.imyzt.learning.feign.MainServiceApplication.TestBean;

/**
 * @author imyzt
 * @date 2021/12/07
 * @description 服务调用方API定义
 */
@FeignClient(name = "provider-api", url = "localhost:8089")
public interface ProviderApi {

    @GetMapping("/service-provider")
    String queryTest(@SpringQueryMap TestBean testBean);
}
