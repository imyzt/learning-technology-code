package top.imyzt.flowable.node;


import org.flowable.bpmn.model.FieldExtension;
import org.flowable.bpmn.model.FlowableListener;
import org.flowable.bpmn.model.ServiceTask;
import org.flowable.bpmn.model.UserTask;
import org.flowable.engine.delegate.BaseExecutionListener;
import org.flowable.task.service.delegate.BaseTaskListener;
import top.imyzt.flowable.FlowContext;
import top.imyzt.flowable.props.Props;
import top.imyzt.flowable.props.TaskProps;

/**
 * @author imyzt
 * @date 2025/05/25
 * @description 描述信息
 */
public class TaskNode extends Node<TaskProps>{
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
        //
        // this.getProps().getExecutionListeners().forEach(listener -> {
        //     FlowableListener flowableListener = new FlowableListener();
        //     /** @see BaseExecutionListener */
        //     flowableListener.setEvent(listener.getEvent());
        //     flowableListener.setImplementationType(listener.getImplementationType());
        //     flowableListener.setImplementation(listener.getImplementation());
        //     serviceTask.getExecutionListeners().add(flowableListener);
        // });

        context.getElementMap().put(this.getId(), serviceTask);


        return this.getId();
    }
}
