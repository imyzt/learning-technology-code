package top.imyzt.flowable.event;

import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.delegate.DelegateExecution;

@Slf4j
public class ProcessEndEventListener implements FlowEventListener {
    
    @Override
    public String getEventType() {
        return "process_end";
    }
    
    @Override
    public void onEvent(DelegateExecution execution) {
        String userId = (String) execution.getVariable("userId");
        String productId = (String) execution.getVariable("productId");
        Long startTime = (Long) execution.getVariable("startTime");
        
        // 计算流程执行时间
        long duration = System.currentTimeMillis() - startTime;
        log.info("流程结束 - 用户[{}]商品[{}]流程执行时间: {}ms", userId, productId, duration);
        
        // 清理流程变量
        execution.removeVariable("startTime");
    }
} 