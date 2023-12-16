package top.imyzt.jdk.features.java13;


import java.time.LocalDate;

/**
 * @author imyzt
 * @date 2023/12/16
 * @description switch 返回值
 */
public class SwitchFuture {

    public static void main(String[] args) {

        int month = LocalDate.now().getMonthValue();
        // java13 之后的switch
        String str = switch (month) {
            case 3, 4, 5 -> "spring";
            case 6, 7, 8 -> "summer";
            case 9, 10, 11 -> "fall";
            case 12, 1, 2 -> "winter";
            default -> "err";
        };
        System.out.println(str);
        // winter

        String strline = """
                第一行
                第二行
                第三行""";
        System.out.println(strline);
        //第一行
        //第二行
        //第三行
    }
}
