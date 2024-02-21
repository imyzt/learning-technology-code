package top.imyzt.learning.dp.statemachine.normal.business.state;


import top.imyzt.learning.dp.statemachine.normal.core.base.BaseStatus;
import top.imyzt.learning.dp.statemachine.normal.core.bean.StateMachine;
import top.imyzt.learning.dp.statemachine.normal.business.event.PaymentEvent;

/**
 * @author imyzt
 * @date 2024/02/21
 * @description 支付状态机
 */
public enum PaymentStatus implements BaseStatus {

    INIT("INIT", "初始化"),
    PAYING("PAYING", "支付中"),
    PAID("PAID", "支付成功"),
    FAILED("FAILED", "支付失败"),

    ;

    /**
     * 状态
     */
    private final String status;

    /**
     * 描述
     */
    private final String desc;

    PaymentStatus(String status, String desc) {
        this.status = status;
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }

    private static final StateMachine<PaymentStatus, PaymentEvent> STATE_MACHINE = new StateMachine<>();
    static {
        // 初始状态
        STATE_MACHINE.accept(null, PaymentEvent.PAY_CREATE, INIT);
        // 支付中
        STATE_MACHINE.accept(INIT, PaymentEvent.PAY_PROCESS, PAYING);
        // 支付成功
        STATE_MACHINE.accept(PAYING, PaymentEvent.PAY_SUCCESS, PAID);
        // 支付失败
        STATE_MACHINE.accept(PAYING, PaymentEvent.PAY_FAIL, FAILED);
    }

    /**
     * 通过源状态和事件类型获取目标状态
     */
    public static PaymentStatus getTargetStatus(PaymentStatus sourceStatus, PaymentEvent event) {
        return STATE_MACHINE.getTargetStatus(sourceStatus, event);
    }
}
