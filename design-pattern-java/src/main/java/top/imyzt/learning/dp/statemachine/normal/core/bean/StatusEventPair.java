package top.imyzt.learning.dp.statemachine.normal.core.bean;


import com.google.common.base.Objects;
import top.imyzt.learning.dp.statemachine.normal.core.base.BaseEvent;
import top.imyzt.learning.dp.statemachine.normal.core.base.BaseStatus;

/**
 * @author imyzt
 * @date 2024/02/21
 * @description 状态事件对, 指定的状态只能接受指定的事件
 */
public class StatusEventPair<S extends BaseStatus, E extends BaseEvent> {

    /**
     * 指定的状态
     */
    private final S status;

    /**
     * 指定的事件
     */
    private final E event;

    public StatusEventPair(S status, E event) {
        this.status = status;
        this.event = event;
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean equals(Object obj) {
        if (obj instanceof StatusEventPair) {
            StatusEventPair<S, E> other = (StatusEventPair<S, E>) obj;
            return Objects.equal(this.status, other.status) && Objects.equal(this.event, other.event);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(status, event);
    }
}
