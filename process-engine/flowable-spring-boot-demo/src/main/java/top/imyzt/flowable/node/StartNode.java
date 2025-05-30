package top.imyzt.flowable.node;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.flowable.bpmn.model.FlowableListener;
import org.flowable.bpmn.model.StartEvent;
import top.imyzt.flowable.FlowContext;
import top.imyzt.flowable.props.StartProps;

/**
 * @author imyzt
 * @date 2025/05/24
 * @description 描述信息
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class StartNode extends Node<StartProps> {
    @Override
    public String convert(FlowContext context) {
        StartEvent startEvent = new StartEvent();
        startEvent.setId(this.getId());
        startEvent.setName(this.getName());

        // 添加事件监听器
        if (this.getProps() != null) {
            // 添加加购事件监听器
            if (this.getProps().getDelegate() != null) {
                FlowableListener flowableListener = new FlowableListener();
                flowableListener.setEvent("start");
                flowableListener.setImplementationType(this.getProps().getDelegate().getImplementationType());
                flowableListener.setImplementation(this.getProps().getDelegate().getImplementation());
                startEvent.getExecutionListeners().add(flowableListener);
            }

            // 添加过滤器
            /* if (this.getProps().getFilters() != null) {
                for (StartProps.Filter filter : this.getProps().getFilters()) {
                    try {
                        FlowFilter flowFilter = (FlowFilter) Class.forName(filter.getClassName()).newInstance();
                        // 设置过滤器属性
                        if (flowFilter instanceof UserPropertyFilter) {
                            UserPropertyFilter propertyFilter = (UserPropertyFilter) flowFilter;
                            propertyFilter.setPropertyName(filter.getPropertyName());
                            propertyFilter.setOperator(filter.getOperator());
                            propertyFilter.setPropertyValue(filter.getPropertyValue());
                        } else if (flowFilter instanceof UserBehaviorFilter) {
                            UserBehaviorFilter behaviorFilter = (UserBehaviorFilter) flowFilter;
                            behaviorFilter.setBehaviorType(filter.getBehaviorType());
                            behaviorFilter.setTargetId(filter.getTargetId());
                            behaviorFilter.setTimeRange(filter.getTimeRange());
                            behaviorFilter.setCount(filter.getCount());
                        }

                        // 添加过滤器监听器
                        FlowableListener filterListener = new FlowableListener();
                        filterListener.setEvent("start");
                        filterListener.setImplementationType("class");
                        filterListener.setImplementation(filter.getClassName());
                        startEvent.getExecutionListeners().add(filterListener);
                    } catch (Exception e) {
                        throw new RuntimeException("创建过滤器失败: " + filter.getClass(), e);
                    }
                }
            } */
        }

        context.getElementMap().put(this.getId(), startEvent);
        return this.getId();
    }
}
