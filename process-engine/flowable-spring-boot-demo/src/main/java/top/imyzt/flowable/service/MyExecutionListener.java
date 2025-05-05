package top.imyzt.flowable.service;


import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.ExecutionListener;

/**
 * @author imyzt
 * @date 2025/05/05
 * @description 描述信息
 */
public class MyExecutionListener implements ExecutionListener {
    @Override
    public void notify(DelegateExecution delegateExecution) {
        System.out.println("我是执行监听器");
    }
}
