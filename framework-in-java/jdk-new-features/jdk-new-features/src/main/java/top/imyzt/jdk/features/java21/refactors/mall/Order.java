package top.imyzt.jdk.features.java21.refactors.mall;


/**
 * @author imyzt
 * @date 2024/10/15
 */
public class Order {

    private final String id;
    private final Customer customer;
    private final double totalAmount;

    public Order(String id, Customer customer, double totalAmount) {
        this.id = id;
        this.customer = customer;
        this.totalAmount = totalAmount;
    }

    public String getId() {
        return id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public double getTotalAmount() {
        return totalAmount;
    }
}
