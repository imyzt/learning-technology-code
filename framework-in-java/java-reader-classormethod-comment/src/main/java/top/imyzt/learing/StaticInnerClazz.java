package top.imyzt.learing;


/**
 * @author imyzt
 * @date 2024/05/13
 * @description 静态内部类,无法引用外部类的属性
 */
public class StaticInnerClazz {

    private final String name;

    public StaticInnerClazz(String name) {
        this.name = name;
    }

    public static class Inner2 {
        public void print2() {
            // Non-static field 'name' cannot be referenced from a static context
            // System.out.println(name);
        }
    }
}
