package top.imyzt.flowable.service;


import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.flowable.common.engine.impl.el.FixedValue;
import org.flowable.engine.delegate.TaskListener;
import org.flowable.task.service.delegate.DelegateTask;

/**
 * @author imyzt
 * @date 2025/05/05
 * @description 描述信息
 */
@Slf4j
@Setter
public class MyTaskListener implements TaskListener {

    private FixedValue param;

    @Override
    public void notify(DelegateTask delegateTask) {
        log.info("我是任务监听器, param={}", param.getExpressionText());
    }
}
