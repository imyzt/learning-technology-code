package top.imyzt.learning.bean.three;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import top.imyzt.learning.bean.three.beandefinition.ImportBeanDefinitionRegistrarImpl;
import top.imyzt.learning.bean.three.selector.ImportSelectorImpl;

/**
 * @author imyzt
 * @date 2021/03/20
 * @description 通过Import注册Bean
 */
@Configuration
@Import(value = {
        Color.class,
        ImportSelectorImpl.class,
        ImportBeanDefinitionRegistrarImpl.class
})
public class ImportBeanTest {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext annotationConfigApplicationContext =
                new AnnotationConfigApplicationContext(ImportBeanTest.class);

        for (String beanDefinitionName : annotationConfigApplicationContext.getBeanDefinitionNames()) {
            System.out.println(beanDefinitionName);
        }
        //importBeanTest

        // 通过Import注册
        //top.imyzt.learning.bean.three.Color

        // 通过ImportSelectorImpl注册
        //top.imyzt.learning.bean.three.selector.Red

        // 通过ImportBeanDefinitionRegistrarImpl注册
        //green
    }


}