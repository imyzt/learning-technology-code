package top.imyzt.jdk.features.java21.refactors.mall;

/**
 * @author imyzt
 * @date 2024/10/15
 * @description 订单计算策略
 */
public sealed interface SpecialOrder permits PhysicalOrder, VirtualOrder {

    /**
     * 计算运费
     * @return 运费
     */
    double calculateShippingCost();
}
