package top.imyzt.flowable.service;


import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;

/**
 * @author imyzt
 * @date 2025/05/24
 * @description 描述信息
 */
public class MyDelegate implements JavaDelegate {
    @Override
    public void execute(DelegateExecution delegateExecution) {
        System.out.println("delegateDemo...");
    }
}
