package top.imyzt.flowable.service;

import lombok.extern.slf4j.Slf4j;
import org.flowable.common.engine.api.delegate.Expression;
import org.flowable.engine.delegate.DelegateExecution;

@Slf4j
public class AddToCartDelegate extends BaseDelegate {

    Expression source;

    @Override
    protected void doExecute(DelegateExecution execution) throws Exception {
        // 模拟从事件中获取用户信息
        String userId = (String) execution.getVariable("userId");
        String productId = (String) execution.getVariable("productId");

        log.info("用户[{}]加购商品[{}]", userId, productId);

        // 设置流程变量
        execution.setVariable("addToCartTime", System.currentTimeMillis());
        execution.setVariable("productId", productId);
        execution.setVariable("userId", userId);

        // 模拟记录加购信息
        log.info("记录加购信息成功");
    }
}
