package top.imyzt.flowable.node;

import org.apache.commons.collections4.CollectionUtils;
import org.flowable.bpmn.model.FieldExtension;
import org.flowable.bpmn.model.FlowableListener;
import org.flowable.bpmn.model.ServiceTask;
import org.flowable.engine.delegate.BaseExecutionListener;
import top.imyzt.flowable.FlowContext;
import top.imyzt.flowable.props.Props;
import top.imyzt.flowable.props.TaskProps;

/**
 * @author imyzt
 * @date 2025/05/25
 * @description 任务节点
 */
public class TaskNode extends Node<TaskProps> {
    @Override
    public String convert(FlowContext context) {
        context.collectTaskNode(this);

        ServiceTask serviceTask = new ServiceTask();
        serviceTask.setId(this.getId());
        serviceTask.setName(this.getName());

        TaskProps props = this.getProps();
        Props.Listener delegate = props.getDelegate();
        serviceTask.setImplementation(delegate.getImplementation());
        serviceTask.setImplementationType(delegate.getImplementationType());

        // 添加参数
        FieldExtension field1 = new FieldExtension();
        field1.setFieldName("source");
        field1.setStringValue(delegate.getParam());
        serviceTask.getFieldExtensions().add(field1);

        // 添加优惠券相关参数
        if (props.getCouponType() != null) {
            FieldExtension couponTypeField = new FieldExtension();
            couponTypeField.setFieldName("couponType");
            couponTypeField.setStringValue(props.getCouponType());
            serviceTask.getFieldExtensions().add(couponTypeField);
        }

        if (props.getCouponValue() != null) {
            FieldExtension couponValueField = new FieldExtension();
            couponValueField.setFieldName("couponValue");
            couponValueField.setStringValue(props.getCouponValue());
            serviceTask.getFieldExtensions().add(couponValueField);
        }

        if (props.getCouponUnit() != null) {
            FieldExtension couponUnitField = new FieldExtension();
            couponUnitField.setFieldName("couponUnit");
            couponUnitField.setStringValue(props.getCouponUnit());
            serviceTask.getFieldExtensions().add(couponUnitField);
        }

        // 添加执行监听器
        if (CollectionUtils.isNotEmpty(this.getProps().getExecutionListeners())) {
            this.getProps().getExecutionListeners().forEach(listener -> {
                FlowableListener flowableListener = new FlowableListener();
                flowableListener.setEvent(listener.getEvent());
                flowableListener.setImplementationType(listener.getImplementationType());
                flowableListener.setImplementation(listener.getImplementation());
                FieldExtension fieldExtension1 = new FieldExtension();
                fieldExtension1.setFieldName("source");
                fieldExtension1.setStringValue(listener.getParam());
                flowableListener.getFieldExtensions().add(fieldExtension1);

                serviceTask.getExecutionListeners().add(flowableListener);
            });
        }

        context.getElementMap().put(this.getId(), serviceTask);

        return this.getId();
    }
}
