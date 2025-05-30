package top.imyzt.flowable.event;

import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.delegate.DelegateExecution;

@Slf4j
public class ProcessStartEventListener implements FlowEventListener {
    
    @Override
    public String getEventType() {
        return "process_start";
    }
    
    @Override
    public void onEvent(DelegateExecution execution) {
        String userId = (String) execution.getVariable("userId");
        String productId = (String) execution.getVariable("productId");
        log.info("流程启动 - 用户[{}]添加商品[{}]到购物车", userId, productId);
        
        // 记录流程启动时间
        execution.setVariable("startTime", System.currentTimeMillis());
    }
} 