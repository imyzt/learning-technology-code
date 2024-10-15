package top.imyzt.jdk.features.java21.refactors.mall;


/**
 * @author imyzt
 * @date 2024/10/15
 * @description 描述信息
 */
public class Main {

    public static void main(String[] args) {
        SpecialOrder physicalOrder = new PhysicalOrder("DIGITAL-001", new Customer("1", "John Doe", "john@example.com"), 99.99, "shenzhen");
        SpecialOrder virtualOrder = new VirtualOrder("VIRTUAL-001", new Customer("2", "Anna", "anna@example.com"), 88.88, "shenzhen");

        System.out.println(physicalOrder.calculateShippingCost());
        System.out.println(virtualOrder.calculateShippingCost());
    }
}
