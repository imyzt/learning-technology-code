package top.imyzt.flowable.props;


import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author imyzt
 * @date 2025/05/24
 * @description 描述信息
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class StartProps extends Props {

    private String start_type;
    private String event_code;


    private List<Listener> taskListeners;
}
