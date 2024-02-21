package top.imyzt.learning.dp.statemachine.normal.business.service;


import top.imyzt.learning.dp.statemachine.normal.business.event.PaymentEvent;
import top.imyzt.learning.dp.statemachine.normal.business.model.PaymentModel;

import static top.imyzt.learning.dp.statemachine.normal.business.state.PaymentStatus.INIT;
import static top.imyzt.learning.dp.statemachine.normal.business.state.PaymentStatus.PAYING;

/**
 * @author imyzt
 * @date 2024/02/21
 * @description 支付领域服务
 */
public class PaymentDomainService {

    public static void main(String[] args) {

        // 模拟1, 下单
        PaymentModel paymentModel1 = new PaymentModel(null, null);
        paymentModel1.transferStatusByEvent(PaymentEvent.PAY_CREATE);
        System.out.println(paymentModel1);

        // 模拟2, 支付
        PaymentModel paymentModel2 = new PaymentModel(paymentModel1.getCurrStatus(), INIT);
        paymentModel2.transferStatusByEvent(PaymentEvent.PAY_PROCESS);
        System.out.println(paymentModel2);

        // 模拟3, 支付成功
        PaymentModel paymentModel3 = new PaymentModel(paymentModel2.getCurrStatus(), PAYING);
        paymentModel3.transferStatusByEvent(PaymentEvent.PAY_SUCCESS);
        System.out.println(paymentModel3);

        // 模拟4, 支付失败
        PaymentModel paymentModel4 = new PaymentModel(paymentModel2.getCurrStatus(), PAYING);
        paymentModel4.transferStatusByEvent(PaymentEvent.PAY_FAIL);
        System.out.println(paymentModel4);

        // 初始化
        // PaymentModel{lastStatus=null, currStatus=INIT}
        // 初始化 -> 支付中
        // PaymentModel{lastStatus=INIT, currStatus=PAYING}
        // 支付中 -> 支付成功
        // PaymentModel{lastStatus=PAYING, currStatus=PAID}
        // 支付中 -> 支付失败
        // PaymentModel{lastStatus=PAYING, currStatus=FAILED}
    }
}
