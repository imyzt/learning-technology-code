package top.imyzt.jdk.features.java12;


import java.time.LocalDate;

/**
 * @author imyzt
 * @date 2023/12/16
 * @description switch
 */
public class SwitchFuture {

    public static void main(String[] args) {

        int month = LocalDate.now().getMonthValue();

        // java12 前 switch
        switch (month) {
            case 3:
            case 4:
            case 5:
                System.out.println("spring");
                break;
            case 6:
            case 7:
            case 8:
                System.out.println("summer");
                break;
            case 9:
            case 10:
            case 11:
                System.out.println("fall");
                break;
            case 12:
            case 1:
            case 2:
                System.out.println("winter");
                break;
            default:
                System.out.println("err");
        }

        // java12 之后的switch
        switch (month) {
            case 3, 4, 5 -> System.out.println("spring");
            case 6, 7, 8 -> System.out.println("summer");
            case 9, 10, 11 -> System.out.println("fall");
            case 12, 1, 2 -> System.out.println("winter");
            default -> System.out.println("err");

        }

    }
}
