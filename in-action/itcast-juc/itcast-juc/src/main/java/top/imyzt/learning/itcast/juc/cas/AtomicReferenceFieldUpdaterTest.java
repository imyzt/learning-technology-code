package top.imyzt.learning.itcast.juc.cas;

import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;

/**
 * @author imyzt
 * @date 2022/08/14
 * @description 字段更新器
 */
public class AtomicReferenceFieldUpdaterTest {

    public static void main(String[] args) {

        Student student = new Student();

        AtomicReferenceFieldUpdater<Student, String> fieldUpdater =
                AtomicReferenceFieldUpdater.newUpdater(Student.class, String.class, "name");

        // 对象, 原始值, 修改之后的值
        System.out.println(fieldUpdater.compareAndSet(student, null, "张三"));
        System.out.println(student);
        //true
        //Student{name='张三'}

        System.out.println(fieldUpdater.compareAndSet(student, "嘿嘿", "张三"));
        System.out.println(student);
        //false
        //Student{name='张三'}
    }
}

class Student {
    volatile String name;

    @Override
    public String toString() {
        return "Student{" +
                "name='" + name + '\'' +
                '}';
    }
}