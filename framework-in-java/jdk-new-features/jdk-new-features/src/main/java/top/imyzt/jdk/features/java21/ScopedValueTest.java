package top.imyzt.jdk.features.java21;


/**
 * @author imyzt
 * @date 2023/12/20
 * @description ScopedValue
 */
public class ScopedValueTest {

    private static final ScopedValue<String> GIFT = ScopedValue.newInstance();

    public static void main(String[] args) {
        ScopedValueTest test = new ScopedValueTest();
        test.giveGift();
        //中间人开始: 500
        //收礼物: 200
        //中间人结束: 500
    }

    private void giveGift() {
        ScopedValue.where(GIFT, "500").run(this::receiveMiddleMan);
    }

    private void receiveMiddleMan() {
        System.out.println(STR."\{Thread.currentThread().getName()} 中间人开始: \{GIFT.get()}");
        ScopedValue.where(GIFT, "200").run(this::receiveGift);
        System.out.println(STR."\{Thread.currentThread().getName()} 中间人结束: \{GIFT.get()}");
    }

    private void receiveGift() {
        System.out.println(STR."\{Thread.currentThread().getName()} 收礼物: \{GIFT.get()}");
    }
}
