package top.imyzt.learning.condition;

import org.springframework.context.annotation.ConditionContext;
import org.springframework.context.annotation.ConfigurationCondition;
import org.springframework.core.annotation.Order;
import org.springframework.core.type.AnnotatedTypeMetadata;

import java.util.Map;

/**
 * @author imyzt
 * @date 2021/01/02
 * @description 判断是否是对应的操作系统
 */
@Order
public class OnOSCondition implements ConfigurationCondition {

    @Override
    public ConfigurationPhase getConfigurationPhase() {
        return ConfigurationPhase.REGISTER_BEAN;
    }

    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {

        Map<String, Object> attributes = metadata.getAnnotationAttributes(ConditionOnOS.class.getName());
        ConditionOnOS.OSType osType = (ConditionOnOS.OSType) attributes.get("osType");

        boolean contains = System.getProperty("os.name").toUpperCase().contains(osType.name());

        return contains;
    }
}