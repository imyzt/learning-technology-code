package top.imyzt.learning.liteflowdemo.aop;


import cn.hutool.core.util.ReflectUtil;
import com.yomahub.liteflow.aop.ICmpAroundAspect;
import com.yomahub.liteflow.core.NodeComponent;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * @author imyzt
 * @date 2025/08/16
 * @description 全局组件拦截器
 */
@Component
@Slf4j
public class CmpAop implements ICmpAroundAspect {
    @Override
    public void beforeProcess(NodeComponent cmp) {

    }

    @SneakyThrows
    @Override
    public void afterProcess(NodeComponent cmp) {
        if (cmp.getClass().getName().contains("$ByteBuddy$")) {
            Object target = ReflectUtil.getFieldValue(cmp, "target");
            // 调用原始 bean 的 customerMethod
            Method m = ReflectUtil.getMethod(target.getClass(), "customerMethod", NodeComponent.class);
            ReflectUtil.invoke(target, m, cmp);
        }
    }

    @Override
    public void onSuccess(NodeComponent cmp) {

    }

    @Override
    public void onError(NodeComponent cmp, Exception e) {

    }
}
