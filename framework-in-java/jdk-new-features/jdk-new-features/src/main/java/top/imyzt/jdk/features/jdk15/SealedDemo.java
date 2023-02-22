package top.imyzt.jdk.features.jdk15;

/**
 * 细粒度的final, 指定谁可以继承
 * @author imyzt
 * @date 2023/02/20
 * @description sealed
 */
public abstract sealed class SealedDemo permits SealedA, SealedB, SealedC {

    abstract void test();
}