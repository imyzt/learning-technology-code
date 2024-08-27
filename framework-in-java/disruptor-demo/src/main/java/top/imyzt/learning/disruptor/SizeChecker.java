package top.imyzt.learning.disruptor;


import org.apache.lucene.util.RamUsageEstimator;

/**
 * @author imyzt
 * @date 2024/08/27
 * @description 检查对象内存大小(bit)
 */
public class SizeChecker {

    public static void main(String[] args) {

        int i = 1;
        System.out.println(RamUsageEstimator.sizeOf(i));
        System.out.println(RamUsageEstimator.sizeOf(1));
        System.out.println(RamUsageEstimator.sizeOf(123L));
        System.out.println(RamUsageEstimator.sizeOf("😄"));
        System.out.println(RamUsageEstimator.sizeOf("s"));
        System.out.println(RamUsageEstimator.sizeOf("中"));

        //16
        //16
        //24
        //48
        //48
        //48

    }
}
