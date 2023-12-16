package top.imyzt.jdk.features.java14;


/**
 * @author imyzt
 * @date 2023/12/16
 * @description 友好的NPE提示
 */
public class NpeFuture {

    public static void main(String[] args) {
        C c = new C();
        // 在链式调用时, 如果有空指针, 可以明确是哪个变量空指针
        String name = c.b.a.name;
        System.out.println(name);
        // Cannot read field "a" because "c.b" is null
    }
}

class A {
    public String name;
}

class B {
    public A a;
}

class C {
    public B b;
}
