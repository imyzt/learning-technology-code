package top.imyzt.learning.dp.statemachine.normal.business.model;


import top.imyzt.learning.dp.statemachine.normal.business.event.PaymentEvent;
import top.imyzt.learning.dp.statemachine.normal.business.state.PaymentStatus;

/**
 * @author imyzt
 * @date 2024/02/21
 * @description 支付单模型
 */
public class PaymentModel {

    /**
     * 上次状态
     */
    private PaymentStatus lastStatus;

    /**
     * 当前状态
     */
    private PaymentStatus currStatus;

    public PaymentStatus getLastStatus() {
        return lastStatus;
    }

    public PaymentStatus getCurrStatus() {
        return currStatus;
    }

    public PaymentModel(PaymentStatus lastStatus, PaymentStatus currStatus) {
        this.lastStatus = lastStatus;
        this.currStatus = currStatus;
    }

    public void transferStatusByEvent(PaymentEvent event) {
        // 根据当前状态和事件，去获取目标状态
        PaymentStatus targetStatus = PaymentStatus.getTargetStatus(currStatus, event);
        // 如果目标状态不为空，说明是可以推进的
        if (targetStatus != null) {
            lastStatus = currStatus;
            currStatus = targetStatus;
        } else {
            // 目标状态为空，说明是非法推进，进入异常处理，这里只是抛出去，由调用者去具体处理
            throw new RuntimeException("状态转换异常");
        }
    }

    @Override
    public String toString() {
        return "PaymentModel{" +
                "lastStatus=" + lastStatus +
                ", currStatus=" + currStatus +
                '}';
    }
}
