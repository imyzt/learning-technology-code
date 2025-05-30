package top.imyzt.flowable.node;


import org.apache.commons.collections4.CollectionUtils;
import org.flowable.bpmn.model.FieldExtension;
import org.flowable.bpmn.model.FlowableListener;
import org.flowable.bpmn.model.ServiceTask;
import org.flowable.engine.delegate.BaseExecutionListener;
import top.imyzt.flowable.FlowContext;
import top.imyzt.flowable.props.Props;
import top.imyzt.flowable.props.StartProps;

/**
 * @author imyzt
 * @date 2025/05/24
 * @description 描述信息
 */
public class StartNode extends Node<StartProps>{
    @Override
    public String convert(FlowContext context) {

        context.collectTaskNode(this);

        ServiceTask serviceTask = new ServiceTask();
        serviceTask.setId(this.getId());
        serviceTask.setName(this.getName());
        // serviceTask.setAssignee("${initiator}");
        serviceTask.setSkipExpression(String.format("${variables:contains('skipList', '%s')}", this.getId()));

        StartProps props = this.getProps();
        Props.Listener delegate = props.getDelegate();
        serviceTask.setImplementation(delegate.getImplementation());
        serviceTask.setImplementationType(delegate.getImplementationType());
        FieldExtension fieldExtension = new FieldExtension();
        fieldExtension.setFieldName("source");
        fieldExtension.setStringValue(delegate.getParam());
        serviceTask.getFieldExtensions().add(fieldExtension);


        if (CollectionUtils.isNotEmpty(this.getProps().getExecutionListeners())) {
            this.getProps().getExecutionListeners().forEach(listener -> {
                FlowableListener flowableListener = new FlowableListener();
                /** @see BaseExecutionListener */
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
