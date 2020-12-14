package top.imyzt.learning.autoconfig;

import org.springframework.boot.autoconfigure.AutoConfigurationImportEvent;
import org.springframework.boot.autoconfigure.AutoConfigurationImportListener;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.core.io.support.SpringFactoriesLoader;

import java.util.List;
import java.util.Set;

/**
 * @author imyzt
 * @date 2020/12/14
 * @description 监听自动配置导入时事件
 */
public class DefaultAutoConfigurationImportListener implements AutoConfigurationImportListener {

    @Override
    public void onAutoConfigurationImportEvent(AutoConfigurationImportEvent event) {

        ClassLoader classLoader = event.getClass().getClassLoader();
        System.out.println("当前classLoader: " + classLoader);

        // 候选的自动装配Class名单
        List<String> candidates = SpringFactoriesLoader.loadFactoryNames(EnableAutoConfiguration.class, classLoader);

        // 真正的自动装配class名单
        List<String> candidateConfigurations = event.getCandidateConfigurations();

        // 排除的自动装配名单
        Set<String> exclusions = event.getExclusions();

        System.out.printf("自动装配class名单 - 候选数量: %d, 实际数量: %d, 排除数量: %d \n", candidates.size(), candidateConfigurations.size(), exclusions.size());

        System.out.println("实际的自动装配class名单: ");
        candidateConfigurations.forEach(System.out::println);

        System.out.println("排除的自动装配class名单: ");
        exclusions.forEach(System.out::println);
    }
}