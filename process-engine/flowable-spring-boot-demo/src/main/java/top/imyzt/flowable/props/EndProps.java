package top.imyzt.flowable.props;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class EndProps extends Props {
    
    private List<EventListener> eventListeners;
    
    @Data
    public static class EventListener {
        private String type;
        private String className;
    }
}