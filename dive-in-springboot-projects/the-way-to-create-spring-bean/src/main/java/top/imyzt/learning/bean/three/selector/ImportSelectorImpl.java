package top.imyzt.learning.bean.three.selector;

import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;

/**
 * @author imyzt
 * @date 2021/03/20
 * @description 导入Bean
 */
public class ImportSelectorImpl implements ImportSelector {
    @Override
    public String[] selectImports(AnnotationMetadata annotationMetadata) {
        return new String[]{"top.imyzt.learning.bean.three.selector.Red"};
    }
}