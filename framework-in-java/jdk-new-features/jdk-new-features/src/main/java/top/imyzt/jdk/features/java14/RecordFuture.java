package top.imyzt.jdk.features.java14;


/**
 * @author imyzt
 * @date 2023/12/16
 * @description record模式
 */
public class RecordFuture {

    public static void main(String[] args) {
        Student student = new Student("yzt", 25);
        System.out.println(student);
        // Student[name=yzt, age=25]

        student.study();
        // good good study!
    }
}

record Student(String name, Integer age) {
    public void study() {
        System.out.println("good good study!");
    }
}

