package top.imyzt.learning.jackson.config;


import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ScannedGenericBeanDefinition;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import top.imyzt.learning.jackson.pojo.dto.BaseParam;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * @author imyzt
 * @date 2021/07/23
 * @description Jackson ObjectMapper 序列化方案配置
 */
@Configuration
public class ObjectMapperConfig {

    @Resource
    private ObjectMapper objectMapper;

    @PostConstruct
    public void registerSubtypes() {

        objectMapper.registerSubtypes(BaseParam.class);

        String packageName = BaseParam.class.getPackage().getName();

        ClassPathScanningCandidateComponentProvider provider = new ClassPathScanningCandidateComponentProvider(false);
        provider.addIncludeFilter(new AnnotationTypeFilter(JsonTypeName.class));

        provider.findCandidateComponents(packageName).forEach(
                beanDefinition -> {
                    AnnotationMetadata metadata = ((ScannedGenericBeanDefinition) beanDefinition).getMetadata();
                    Class<?> dataStructClass;
                    try {
                        dataStructClass = Class.forName(metadata.getClassName());
                    } catch (ClassNotFoundException e) {
                        throw new RuntimeException("create instance error, " + metadata.getClassName());
                    }
                    JsonTypeName jsonTypeName = dataStructClass.getAnnotation(JsonTypeName.class);

                    objectMapper.registerSubtypes(new NamedType(dataStructClass, jsonTypeName.value()));

                }
        );
    }
}
