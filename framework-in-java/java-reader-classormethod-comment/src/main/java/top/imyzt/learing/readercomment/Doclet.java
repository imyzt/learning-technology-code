package top.imyzt.learing.readercomment;


import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.FieldDoc;
import com.sun.javadoc.MethodDoc;
import com.sun.javadoc.RootDoc;
import com.sun.org.slf4j.internal.Logger;
import com.sun.org.slf4j.internal.LoggerFactory;
import org.jooq.tools.reflect.Reflect;

/**
 * @author imyzt
 * @date 2024/04/10
 * @description 类/方法 注释读取类
 */
public class Doclet {
    public static Logger logger = LoggerFactory.getLogger(Doclet.class);

    private static RootDoc rootDoc;
    private final String clsFilePath;

    public static boolean start(RootDoc root) {
        rootDoc = root;
        return true;
    }

    public Doclet(String clsFilePath) {
        this.clsFilePath = clsFilePath;
    }

    public void exec() {
        com.sun.tools.javadoc.Main.execute(
                new String[]{"-doclet", Doclet.class.getName(),
                        "-docletpath", Doclet.class.getResource("/").getPath(),
                        "-encoding", "utf-8",
                        clsFilePath});
        ClassDoc[] classes = rootDoc.classes();

        if (classes == null || classes.length == 0) {
            logger.warn(clsFilePath + " 无ClassDoc信息");
            return;
        }

        ClassDoc classDoc = classes[0];
        // 获取类的名称
        System.err.println("类名：" + classDoc.name());
        // 获取类的注释
        String classComment = Reflect.on(classDoc).field("documentation").get().toString();
        System.err.println("类注释：" + classComment);
        // 获取属性名称和注释
        for (FieldDoc field : classDoc.fields(false)) {
            System.err.printf("属性名：%s, 属性类型：%s, 注释：%s%n", field.name(), field.type().typeName(), field.commentText());
        }

        for (MethodDoc method : classDoc.methods(false)) {
            System.err.printf("方法名：%s, 方法返回类型：%s, 注释：%s%n", method.name(), method.returnType().typeName(), method.commentText());
        }
    }

}
