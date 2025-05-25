package top.imyzt.flowable.node;


import org.flowable.bpmn.model.FieldExtension;
import org.flowable.bpmn.model.FlowableListener;
import org.flowable.bpmn.model.UserTask;
import org.flowable.task.service.delegate.BaseTaskListener;
import top.imyzt.flowable.FlowContext;
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

        UserTask userTask = new UserTask();
        userTask.setId(this.getId());
        userTask.setName(this.getName());
        userTask.setAssignee("${initiator}");
        userTask.setSkipExpression(String.format("${variables:contains('skipList', '%s')}", this.getId()));

        this.getProps().getTaskListeners().forEach(listener -> {
            FlowableListener flowableListener = new FlowableListener();
            /** @see BaseTaskListener */
            flowableListener.setEvent(listener.getEvent());
            flowableListener.setImplementationType(listener.getImplementationType());
            flowableListener.setImplementation(listener.getImplementation());

            FieldExtension fieldExtension = new FieldExtension();
            fieldExtension.setFieldName("param");
            fieldExtension.setStringValue(listener.getParam());
            flowableListener.getFieldExtensions().add(fieldExtension);

            userTask.getTaskListeners().add(flowableListener);
        });

        context.getElementMap().put(this.getId(), userTask);

        return this.getId();
    }
}
