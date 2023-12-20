package top.imyzt.jdk.features.java21;


/**
 * @author imyzt
 * @date 2023/12/20
 * @description Switch 又升级了~, 这次支持when关键字了
 */
public class SwitchFuture {

    void main() {

        var str = "yes";

        var result = switch (str) {
            case null -> "空对象";
            case String s
                    when "yes".equals(s) -> {
                System.out.println("确定");
                yield "字符串的Yes";
            }
            case String s
                    when "no".equals(s) -> {
                System.out.println("取消");
                yield "字符串的No";
            }
            default -> "default";
        };

        System.out.println(result);
        //确定
        //字符串的Yes
    }
}
