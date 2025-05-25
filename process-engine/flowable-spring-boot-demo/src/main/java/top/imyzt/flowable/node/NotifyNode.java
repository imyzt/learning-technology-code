package top.imyzt.flowable.node;


import org.flowable.bpmn.model.ImplementationType;
import org.flowable.bpmn.model.ServiceTask;
import top.imyzt.flowable.FlowContext;
import top.imyzt.flowable.props.NotifyProps;

/**
 * @author imyzt
 * @date 2025/05/24
 * @description 描述信息
 */
public class NotifyNode extends Node<NotifyProps>{
    @Override
    public String convert(FlowContext context) {

        context.collectTaskNode(this);

        ServiceTask serviceTask = new ServiceTask();
        serviceTask.setId(this.getId());
        serviceTask.setName(this.getName());
        // serviceTask.setType("serviceTask");
        serviceTask.setImplementationType(ImplementationType.IMPLEMENTATION_TYPE_DELEGATEEXPRESSION);
        serviceTask.setImplementation(String.format("${%s}", "notifyDelegate"));



        context.getElementMap().put(this.getId(), serviceTask);

        return this.getId();
    }
}
