package top.imyzt.flowable.node;

import org.flowable.bpmn.model.ExclusiveGateway;
import org.flowable.bpmn.model.SequenceFlow;
import top.imyzt.flowable.FlowContext;
import top.imyzt.flowable.props.GatewayProps;

public class GatewayNode extends Node<GatewayProps> {
    
    @Override
    public String convert(FlowContext context) {
        ExclusiveGateway gateway = new ExclusiveGateway();
        gateway.setId(this.getId());
        gateway.setName(this.getName());
        
        // 添加条件序列流
        if (this.getProps() != null && this.getProps().getConditions() != null) {
            this.getProps().getConditions().forEach(condition -> {
                if (condition.getNext() != null) {
                    SequenceFlow sequenceFlow = new SequenceFlow();
                    sequenceFlow.setId(condition.getId());
                    sequenceFlow.setName(condition.getName());
                    sequenceFlow.setSourceRef(this.getId());
                    sequenceFlow.setTargetRef(condition.getNext());
                    sequenceFlow.setConditionExpression(condition.getCondition());
                    gateway.getOutgoingFlows().add(sequenceFlow);
                    context.getSequences().add(sequenceFlow);
                }
            });
        }
        
        context.getElementMap().put(this.getId(), gateway);
        return this.getId();
    }
} 