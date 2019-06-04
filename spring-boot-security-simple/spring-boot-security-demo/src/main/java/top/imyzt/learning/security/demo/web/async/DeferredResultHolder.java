package top.imyzt.learning.security.demo.web.async;

import lombok.Data;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.HashMap;
import java.util.Map;

/**
 * @author imyzt
 * @date 2019/6/4
 * @description DeferredResultHolder
 */
@Component
@Data
public class DeferredResultHolder {

    /**
     * 订单号 -> 订单处理结果
     */
    private Map<String, DeferredResult<String>> map = new HashMap<>(1);

}
