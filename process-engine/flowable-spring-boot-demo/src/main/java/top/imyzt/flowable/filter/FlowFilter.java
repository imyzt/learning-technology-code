package top.imyzt.flowable.filter;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;

/**
 * 流程过滤器接口
 */
public interface FlowFilter  {

    /**
     * 过滤器名称
     */
    String getName();

    /**
     * 执行过滤
     * @param execution 流程执行上下文
     * @return true: 通过过滤, false: 不通过
     */
    boolean filter(DelegateExecution execution);
}
