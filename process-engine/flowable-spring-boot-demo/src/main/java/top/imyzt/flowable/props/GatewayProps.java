package top.imyzt.flowable.props;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class GatewayProps extends Props {
    
    private String type;
    private List<Condition> conditions;
    
    @Data
    public static class Condition {
        private String id;
        private String name;
        private String condition;
        private String next;
    }
} 