package top.imyzt.flowable.service;


import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.ExecutionListener;

/**
 * @author imyzt
 * @date 2025/05/05
 * @description 描述信息
 */
@Slf4j
public class MyExecutionListener implements ExecutionListener {
    @Override
    public void notify(DelegateExecution delegateExecution) {
        String param = (String) delegateExecution.getVariable("param");
        log.info("我是执行监听器: {}", param);
    }
}
