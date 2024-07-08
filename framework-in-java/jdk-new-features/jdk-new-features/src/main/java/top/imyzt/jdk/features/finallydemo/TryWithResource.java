package top.imyzt.jdk.features.finallydemo;


import java.io.FileInputStream;
import java.io.IOException;

/**
 * @author imyzt
 * @date 2024/07/08
 * @description 描述信息
 */
public class TryWithResource {

    public static void main(String[] args) {

        // 常规方式
        normal();
        // 简洁方式
        tryWithResource();
    }

    private static void tryWithResource() {
        try (FileInputStream fis = new FileInputStream("f2")) {
            int i = fis.read() / 0;
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

    private static void normal() {
        try {
            FileInputStream fis = new FileInputStream("f1");
            try {
                int i = fis.read() / 0;
            } catch (IOException e) {
                try {
                    fis.close();
                } catch (IOException ex) {
                    // 关闭流本身也会异常,需要catch处理为业务异常抛出
                    e.addSuppressed(ex);
                }
                throw new RuntimeException();
            }
            fis.close();
        } catch (IOException e) {
            // 读取文件存在异常, 需要catch处理为业务异常抛出
            throw new RuntimeException(e);
        }
    }
}
