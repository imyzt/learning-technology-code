package top.imyzt.flowable.service;

import lombok.extern.slf4j.Slf4j;
import org.flowable.common.engine.api.delegate.Expression;
import org.flowable.engine.delegate.DelegateExecution;

@Slf4j
public class CheckOrderDelegate extends BaseDelegate {

    Expression source;

    @Override
    protected void doExecute(DelegateExecution execution) throws Exception {
        String userId = (String) execution.getVariable("userId");
        String productId = (String) execution.getVariable("productId");

        log.info("检查用户[{}]是否已下单商品[{}]", userId, productId);

        // 模拟检查订单状态
        boolean hasOrdered = mockCheckOrderStatus(userId, productId);

        // 设置流程变量
        execution.setVariable("hasOrdered", hasOrdered);

        if (hasOrdered) {
            log.info("用户[{}]已下单，流程结束", userId);
            execution.setVariable("flowStatus", "COMPLETED");
        } else {
            log.info("用户[{}]未下单，继续执行优惠券发放流程", userId);
            execution.setVariable("flowStatus", "CONTINUE");
        }
    }

    private boolean mockCheckOrderStatus(String userId, String productId) {
        // 模拟检查订单状态，这里随机返回true或false
        return Math.random() > 0.5;
    }
}
