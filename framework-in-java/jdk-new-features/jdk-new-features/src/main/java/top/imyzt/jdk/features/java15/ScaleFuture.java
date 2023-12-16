package top.imyzt.jdk.features.java15;


/**
 * @author imyzt
 * @date 2023/12/16
 * @description scale, permits
 */
public class ScaleFuture {
}

/**
 * 只希望Dog和Cat能继承, 需要使用sealed声明, 使用permits指定
 */
sealed class Animal permits Dog, Cat {

}

/**
 * 继承密封类,必须指定自己为final(Dog), 或继续将自己指定为密封类(Cat)
 */
final class Dog extends Animal {

}

/**
 * 将自己指定为密封类, 并且通过permits指定只有Cat2能够继承
 */
sealed class Cat extends Animal permits Cat2 {

}

final class Cat2 extends Cat {

}
