package top.imyzt.learing;


/**
 * @author imyzt
 * @date 2024/05/13
 * @description 描述信息
 */
public class NonStaticFieldClazz {


    // 成员变量-匿名内部类的非static实例
    private Inner3 inner3_1 = new Inner3() {
        private Integer field_111;
    };
    // 成员变量-非静态内部类的非static实例
    private Inner3 inner3_2 = new Inner3();

    public void print() {

        // 局部变量-匿名内部类的非static实例
        Inner3 inner3_3 = new Inner3() {
            private Integer field_333;
        };

        // 局部变量-非静态内部类的非static实例
        Inner3 inner3_4 = new Inner3();

    }

    public class Inner3 {

    }
}
