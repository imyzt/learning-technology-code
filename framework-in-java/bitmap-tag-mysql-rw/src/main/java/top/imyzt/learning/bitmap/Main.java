package top.imyzt.learning.bitmap;

import org.roaringbitmap.RoaringBitmap;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author imyzt
 * @date 2025/06/12
 * @description 描述信息
 */
public class Main {

    // 数据库地址
    private static final String URL = "jdbc:mysql://localhost:3306/bitmap_tag";
    // 连接数据库用的驱动
    private static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    // 数据库用户名
    private static final String USER = "root";
    // 数据库密码
    private static final String PASSWORD = "12345678";

    static int tagId = 1;


    public static void main(String[] args) throws Exception {

        // Step 1: 加载数据库驱动
        Class.forName(JDBC_DRIVER);

        // Step 2: 获取数据库连接对象
        Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);

        int init = 100;
        for (int i = 0; i < 5; i++) {
            test(connection, init *= 10);
        }
    }

    private static void test(Connection connection, int count) throws Exception {
        // 创建 bitmap 并添加数据
        RoaringBitmap bitmap = new RoaringBitmap();

        // 生成10w个随机数,最大id 五千万, 不能重复
        Set<Integer> set = new HashSet<>();
        for (int i = 0; i < count; i++) {
            int num = ThreadLocalRandom.current().nextInt(50000000);
            while (set.contains(num)) {
                num = ThreadLocalRandom.current().nextInt(50000000);
            }
            bitmap.add(num);
            set.add(num);
        }


        BitmapToMySQL.saveBitmap(tagId, bitmap, set.size(), connection);

        // 模拟写入到 MySQL 的 BLOB 字段
        // ---- JDBC写入略（参考下一节） ----

        // 反序列化回 RoaringBitmap
        // ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
        // RoaringBitmap deserializedBitmap = new RoaringBitmap();
        // deserializedBitmap.deserialize(new DataInputStream(bis));
        RoaringBitmap deserializedBitmap = BitmapToMySQL.loadBitmap(tagId++, connection);

        // 打印内容
        // System.out.println("Loaded bitmap: " + deserializedBitmap);
        System.out.println("Loaded bitmap count : " + deserializedBitmap.getCardinality());
    }
}
