package top.imyzt.flowable.node;


import org.flowable.bpmn.model.EventDefinition;
import org.flowable.bpmn.model.FlowableListener;
import org.flowable.bpmn.model.ImplementationType;
import org.flowable.bpmn.model.IntermediateCatchEvent;
import org.flowable.bpmn.model.TimerEventDefinition;
import org.springframework.util.StringUtils;
import top.imyzt.flowable.FlowContext;
import top.imyzt.flowable.props.TimerProps;
import top.imyzt.flowable.service.MyExecutionListener;

import java.util.ArrayList;
import java.util.List;

/**
 * @author imyzt
 * @date 2025/05/24
 * @description 描述信息
 */
public class TimerNode extends Node<TimerProps>{
    @Override
    public String convert(FlowContext context) {

        context.collectTaskNode(this);

        TimerEventDefinition timerEventDefinition = new TimerEventDefinition();

        TimerProps props = this.getProps();

        switch (props.getWaitType()) {
            case "DURATION"->timerEventDefinition.setTimeDuration(String.format(props.getUnit(), props.getDuration()));
            case "DATE"-> {
                String replace = props.getTimeDate().replace(" ", "T");
                timerEventDefinition.setTimeDate(replace);
            }
        }

        List<EventDefinition> eventDefinitions = new ArrayList<>();
        eventDefinitions.add(timerEventDefinition);

        IntermediateCatchEvent intermediateCatchEvent = new IntermediateCatchEvent();
        intermediateCatchEvent.setId(this.getId());
        intermediateCatchEvent.setName(this.getName());
        intermediateCatchEvent.setEventDefinitions(eventDefinitions);

        FlowableListener flowableListener = new FlowableListener();
        flowableListener.setEvent("start");
        flowableListener.setImplementationType(ImplementationType.IMPLEMENTATION_TYPE_CLASS);
        flowableListener.setImplementation("top.imyzt.flowable.service.MyExecutionListener");
        intermediateCatchEvent.getExecutionListeners().add(flowableListener);

        context.getElementMap().put(intermediateCatchEvent.getId(), intermediateCatchEvent);

        return this.getId();
    }
}
