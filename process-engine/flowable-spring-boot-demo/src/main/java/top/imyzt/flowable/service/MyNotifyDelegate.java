package top.imyzt.flowable.service;


import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

/**
 * @author imyzt
 * @date 2025/05/29
 * @description 描述信息
 */
@Slf4j
@Component
public class MyNotifyDelegate implements JavaDelegate {
    @Override
    public void execute(DelegateExecution execution) {
        log.info("执行通知委托方法");
    }
}
