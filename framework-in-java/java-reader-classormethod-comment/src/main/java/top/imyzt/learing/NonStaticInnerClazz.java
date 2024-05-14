package top.imyzt.learing;


/**
 * @author imyzt
 * @date 2024/05/13
 * @description 非静态内部类,匿名持有外部类的引用
 */
public class NonStaticInnerClazz {

    private final String name;

    public NonStaticInnerClazz(String name) {
        this.name = name;
    }

    public class Inner {

        public void print() {
            System.out.println(name);
        }
    }
}
