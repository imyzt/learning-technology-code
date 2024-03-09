package top.imyzt.learning.caseanalysis.ttl;

import cn.hutool.core.thread.ThreadUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

@Slf4j
public class AlibabaTtlWrongUsageExampleApplication {

    public static void main(String[] args) {
        for (int i = 0; i < 100; i++) {
            demo(i);
        }
        // 14:39:14.412 [main] WARN top.imyzt.learning.caseanalysis.ttl.AlibabaTtlWrongUsageExampleApplication -- lastGetNullValue, idx=0
        // 14:39:14.428 [main] WARN top.imyzt.learning.caseanalysis.ttl.AlibabaTtlWrongUsageExampleApplication -- lastGetNullValue, idx=2
        // 14:39:14.429 [main] WARN top.imyzt.learning.caseanalysis.ttl.AlibabaTtlWrongUsageExampleApplication -- lastGetNullValue, idx=4
        // 14:39:14.429 [main] WARN top.imyzt.learning.caseanalysis.ttl.AlibabaTtlWrongUsageExampleApplication -- lastGetNullValue, idx=6
        // 14:39:14.430 [main] WARN top.imyzt.learning.caseanalysis.ttl.AlibabaTtlWrongUsageExampleApplication -- lastGetNullValue, idx=10
        // ...
    }

    private static void demo(int idx) {
        // 初始化
        AbilityContext.initContext();
        // 赋业务值
        AbilityContext.putValue("main", "mainValue");

        // 主线程获取hashCode
        final int hashCode = AbilityContext.getInnerMap().hashCode();
        ThreadUtil.execute(() -> {
            // 子线程对比hashCode
            log.info("{}, ThreadUtil hashCode={}", idx, AbilityContext.getInnerMap().hashCode() == hashCode);
            // 子线程再次初始化(错误的根源)
            AbilityContext.initContext();
            // do something
        });

        // do something

        // 主线程再次获取业务值(偶现为null)
        String value = AbilityContext.getValue("main");
        if (Objects.isNull(value)) {
            log.warn("lastGetNullValue, idx={}", idx);
        }
    }
}
