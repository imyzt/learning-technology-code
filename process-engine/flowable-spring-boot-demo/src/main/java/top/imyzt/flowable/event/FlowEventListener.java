package top.imyzt.flowable.event;

import org.flowable.engine.delegate.DelegateExecution;

/**
 * 流程事件监听器接口
 */
public interface FlowEventListener {
    
    /**
     * 事件类型
     */
    String getEventType();
    
    /**
     * 处理事件
     * @param execution 流程执行上下文
     */
    void onEvent(DelegateExecution execution);
} 