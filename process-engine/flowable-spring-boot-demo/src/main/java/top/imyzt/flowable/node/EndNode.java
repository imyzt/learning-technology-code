package top.imyzt.flowable.node;

import org.flowable.bpmn.model.EndEvent;
import top.imyzt.flowable.FlowContext;
import top.imyzt.flowable.props.Props;

public class EndNode extends Node<Props> {
    
    @Override
    public String convert(FlowContext context) {
        EndEvent endEvent = new EndEvent();
        endEvent.setId(this.getId());
        endEvent.setName(this.getName());
        
        context.getElementMap().put(this.getId(), endEvent);
        return this.getId();
    }
} 