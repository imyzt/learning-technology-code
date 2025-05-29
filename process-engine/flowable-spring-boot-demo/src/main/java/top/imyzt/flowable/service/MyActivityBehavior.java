package top.imyzt.flowable.service;


import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.impl.delegate.ActivityBehavior;

/**
 * @author imyzt
 * @date 2025/05/29
 * @description 描述信息
 */
@Slf4j
public class MyActivityBehavior implements ActivityBehavior {
    @Override
    public void execute(DelegateExecution execution) {
        log.info("myActivityBehavior...");
    }
}
