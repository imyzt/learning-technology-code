package top.imyzt.jdk.features.finallydemo;


/**
 * @author imyzt
 * @date 2024/07/08
 * @description finally中return对程序造成不可预知的错误
 */
public class FinallyDemo {

    public static void main(String[] args) {

        System.out.println(div(1, 0));
        // 异常被丢弃, 直接返回了"111"
        // 111
        System.out.println(div2(1, 0));
        // div2 finally
        // Exception in thread "main" java.lang.RuntimeException: 算术运算错误
        // 	at top.imyzt.jdk.features.finallydemo.FinallyDemo.div2(FinallyDemo.java:30)
        // 	at top.imyzt.jdk.features.finallydemo.FinallyDemo.main(FinallyDemo.java:13)
    }

    private static int div(int a, int b) {
        try {
            return a / b;
        } catch (Exception e) {
            throw new RuntimeException("算术运算错误");
        } finally {
            return 111;
        }
    }

    private static int div2(int a, int b) {
        try {
            return a / b;
        } catch (Exception e) {
            throw new RuntimeException("算术运算错误");
        } finally {
            System.out.println("div2 finally");
        }
    }
}
