package top.imyzt.jdk.features.java9;


import java.io.FileNotFoundException;

/**
 * @author imyzt
 * @date 2023/12/16
 * @description 1. try 代码块简化, 2. 不可以再使用 "_"(下划线)命名变量
 */
public class TryWithResource {

    public static void main(String[] args) throws FileNotFoundException {

//        // java 8
//        try (FileInputStream fis1 = new FileInputStream("");
//             FileOutputStream fos1 = new FileOutputStream("")) {
//            fis1.read();
//        } catch (Exception e) {
//
//        }
//
//        // java 9
//        // 可以将变量写到 try 代码块中, 让代码块更简洁
//        FileInputStream fis2 = new FileInputStream("");
//        FileOutputStream fos2 = new FileOutputStream("");
//        try (fis2; fos2) {
//
//        } catch (Exception e) {
//
//        }

        String string = "好";

        String s = new String("好");
        System.out.println(s);
        System.out.println(string.toCharArray()[0]);


        // java 9 运行报错 As of Java 9, '_' is a keyword, and may not be used as an identifier
        // String _ = "123";

    }

    /**
     * `@Deprecated` 注解支持指定废弃版本(since), 以及标记未来是否废弃(forRemoval)
     */
    @Deprecated(since = "9", forRemoval = true)
    private void test() {

    }
}
