package top.imyzt.flowable.node;

import cn.hutool.core.util.IdUtil;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeId;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.flowable.bpmn.model.SequenceFlow;
import org.springframework.util.StringUtils;
import top.imyzt.flowable.FlowContext;

import java.io.Serial;
import java.io.Serializable;


@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "type", defaultImpl = EmptyNode.class, visible = true)
@JsonSubTypes({
        @JsonSubTypes.Type(value = StartNode.class, name = "Start"),
        @JsonSubTypes.Type(value = TimerNode.class, name = "Delay"),
        @JsonSubTypes.Type(value = NotifyNode.class, name = "Notify"),
        @JsonSubTypes.Type(value = TaskNode.class, name = "MyTask"),
        @JsonSubTypes.Type(value = GatewayNode.class, name = "Gateway"),
        @JsonSubTypes.Type(value = EndNode.class, name = "End")
})
public abstract class Node<T> implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 节点类型
     */
    @JsonTypeId
    private String type;

    /**
     * 节点ID
     */
    private String id;

    /**
     * 节点名称
     */
    private String name;

    /**
     * 节点配置属性
     */
    private T props;

    public abstract String convert(FlowContext context);

    public static String generateId(String prefix) {
        return prefix + IdUtil.getSnowflakeNextIdStr();
    }

    public static SequenceFlow createSequenceFlow(String sourceRef, String targetRef) {
        return createSequenceFlow(null, sourceRef, targetRef, null);
    }

    public static SequenceFlow createSequenceFlow(String id, String sourceRef, String targetRef, String condition) {
        SequenceFlow seq = new SequenceFlow(sourceRef, targetRef);

        if (StringUtils.hasText(id)) {
            seq.setId(id);
        } else {
            seq.setId(generateId("sid-"));
        }

        if (StringUtils.hasText(condition)) {
            seq.setConditionExpression(condition);
        }
        return seq;
    }

}
