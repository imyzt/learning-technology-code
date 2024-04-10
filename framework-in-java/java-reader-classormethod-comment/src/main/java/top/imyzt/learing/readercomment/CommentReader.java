package top.imyzt.learing.readercomment;


/**
 * @author imyzt
 * @date 2024/04/10
 * @description 读取类和方法的注释
 */
public class CommentReader {

    public static void main(String[] args) {
        Doclet doclet = new Doclet("/Users/.../java-reader-classormethod-comment/src/main/java/top/imyzt/learing/readercomment/Demo.java");
        doclet.exec();
        // 正在构造 Javadoc 信息...
        // 类名：Demo
        // 类注释： 类注释
        //  @author imyzt
        //  @date 2024/04/10
        //
        // 方法名：demo, 方法返回类型：void, 注释：方法注释
    }
}
