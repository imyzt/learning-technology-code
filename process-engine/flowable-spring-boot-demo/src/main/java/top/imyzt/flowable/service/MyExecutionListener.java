package top.imyzt.flowable.service;


import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.flowable.common.engine.api.delegate.Expression;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.ExecutionListener;

/**
 * @author imyzt
 * @date 2025/05/05
 * @description 描述信息
 */
@Slf4j
@Setter
public class MyExecutionListener implements ExecutionListener {

    private Expression source;

    @Override
    public void notify(DelegateExecution delegateExecution) {
        log.info("我是执行监听器: {}", source.getValue(delegateExecution));
    }
}
