package top.imyzt.flowable.service;

import lombok.extern.slf4j.Slf4j;
import org.flowable.common.engine.api.delegate.Expression;
import org.flowable.engine.delegate.DelegateExecution;

@Slf4j
public class SendCouponDelegate extends BaseDelegate {

    Expression source;
    Expression couponType;
    Expression couponValue;
    Expression couponUnit;

    @Override
    protected void doExecute(DelegateExecution execution) throws Exception {
        String userId = (String) execution.getVariable("userId");
        String productId = (String) execution.getVariable("productId");
        
        // 获取优惠券相关参数
        
        log.info("开始为用户[{}]发放商品[{}]的优惠券", userId, productId);
        log.info("优惠券类型: {}, 面值: {}, 单位: {}", couponType.getValue(execution), couponValue.getValue(execution), couponUnit.getValue(execution));
        
        // 模拟发放优惠券
        String couponCode = mockSendCoupon(execution);
        
        // 设置流程变量
        execution.setVariable("couponCode", couponCode);
        
        log.info("优惠券发放成功，券码：{}", couponCode);
    }

    private String mockSendCoupon(DelegateExecution execution) {
        // 模拟生成优惠券码
        return String.format("COUPON_%s_%s_%s_%s", 
            couponType.getValue(execution), 
            couponValue.getValue(execution), 
            couponUnit.getValue(execution), 
            System.currentTimeMillis());
    }
}
