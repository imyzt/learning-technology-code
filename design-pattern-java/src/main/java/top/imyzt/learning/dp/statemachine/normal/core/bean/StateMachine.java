package top.imyzt.learning.dp.statemachine.normal.core.bean;


import top.imyzt.learning.dp.statemachine.normal.core.base.BaseEvent;
import top.imyzt.learning.dp.statemachine.normal.core.base.BaseStatus;

import java.util.HashMap;
import java.util.Map;

/**
 * @author imyzt
 * @date 2024/02/21
 * @description 状态机
 */
public class StateMachine<S extends BaseStatus, E extends BaseEvent> {

    private final Map<StatusEventPair<S, E>, S> statusEventMap = new HashMap<>();

    /**
     * 只接受指定的当前状态下，指定的事件触发，可以到达的指定目标状态
     */
    public void accept(S sourceStatus, E event, S targetStatus) {
        statusEventMap.put(new StatusEventPair<>(sourceStatus, event), targetStatus);
    }

    /**
     * 通过源状态和事件，获取目标状态
     */
    public S getTargetStatus(S sourceStatus, E event) {
        return statusEventMap.get(new StatusEventPair<>(sourceStatus, event));
    }
}
