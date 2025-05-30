package top.imyzt.flowable.props;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author imyzt
 * @date 2025/05/24
 * @description 描述信息
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class StartProps extends Props {

    @JsonProperty("start_type")
    private String startType;
    
    @JsonProperty("event_code")
    private String eventCode;

    private List<Filter> filters;

    @Data
    public static class Filter {
        private String type;
        @JsonProperty("class")
        private String className;
        
        // 用户属性过滤器属性
        private String propertyName;
        private String operator;
        private String propertyValue;
        
        // 用户行为过滤器属性
        private String behaviorType;
        private String targetId;
        private String timeRange;
        private Integer count;
    }
}
