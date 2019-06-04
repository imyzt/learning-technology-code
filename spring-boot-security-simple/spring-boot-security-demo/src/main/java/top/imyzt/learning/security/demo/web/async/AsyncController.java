package top.imyzt.learning.security.demo.web.async;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

/**
 * @author imyzt
 * @date 2019/6/4
 * @description AsyncController
 */
@RestController
@RequestMapping("async")
@Slf4j
public class AsyncController {

    @Autowired
    private MockQueue mockQueue;

    @Autowired
    private DeferredResultHolder deferredResultHolder;

    @GetMapping("order")
    public Callable<String> order() {

        log.info("主线程开始");

        Callable<String> callable = () -> {

            log.info("副线程开始");
            TimeUnit.SECONDS.sleep(1);
            log.info("副线程返回");

            return "success";
        };
        log.info("主线程返回");

        return callable;
    }

    @GetMapping("order_deferred_result")
    public DeferredResult<String> deferredResult() throws InterruptedException {

        log.info("主线程开始");

        // 订单号
        String orderNumber = RandomStringUtils.randomNumeric(10);
        mockQueue.setPlaceOrder(orderNumber);

        // 模拟加入队列
        DeferredResult<String> deferredResult = new DeferredResult<>();
        deferredResultHolder.getMap().put(orderNumber, deferredResult);

        log.info("主线程返回");
        return deferredResult;
    }
}
