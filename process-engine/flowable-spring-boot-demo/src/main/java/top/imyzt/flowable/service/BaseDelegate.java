package top.imyzt.flowable.service;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;

public abstract class BaseDelegate implements JavaDelegate {
    
    @Override
    public void execute(DelegateExecution execution) {
        try {
            doExecute(execution);
        } catch (Exception e) {
            handleException(execution, e);
        }
    }

    protected abstract void doExecute(DelegateExecution execution) throws Exception;

    protected void handleException(DelegateExecution execution, Exception e) {
        execution.setVariable("error", e.getMessage());
        throw new RuntimeException("流程执行异常: " + e.getMessage(), e);
    }
} 