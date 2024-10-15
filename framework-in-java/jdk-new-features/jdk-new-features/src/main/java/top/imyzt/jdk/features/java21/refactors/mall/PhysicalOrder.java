package top.imyzt.jdk.features.java21.refactors.mall;

/**
 * @author imyzt
 * @date 2024/10/15
 * @description 实物商品订单
 */
public non-sealed class PhysicalOrder extends Order implements SpecialOrder {

    private final String shippingAddress;

    public PhysicalOrder(String id, Customer customer, double totalAmount, String shippingAddress) {
        super(id, customer, totalAmount);
        this.shippingAddress = shippingAddress;
    }

    @Override
    public double calculateShippingCost() {
        // 实物商品需要计算运费
        return 9.9;
    }

    public String getShippingAddress() {
        return shippingAddress;
    }
}
