package top.imyzt.jdk.features.java21.refactors.mall;

/**
 * @author imyzt
 * @date 2024/10/15
 * @description 虚拟商品订单
 */
public non-sealed class VirtualOrder extends Order implements SpecialOrder {

    private final String productKey;

    public VirtualOrder(String id, Customer customer, double totalAmount, String productKey) {
        super(id, customer, totalAmount);
        this.productKey = productKey;
    }

    @Override
    public double calculateShippingCost() {
        // 虚拟商品无需运费
        return 0;
    }

    public String getProductKey() {
        return productKey;
    }
}
