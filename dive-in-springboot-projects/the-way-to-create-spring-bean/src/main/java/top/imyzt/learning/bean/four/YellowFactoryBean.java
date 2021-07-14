package top.imyzt.learning.bean.four;

import org.springframework.beans.factory.FactoryBean;

/**
 * @author imyzt
 * @date 2021/03/20
 * @description 描述信息
 */
public class YellowFactoryBean implements FactoryBean<Yellow> {
    @Override
    public Yellow getObject() throws Exception {
        return new Yellow();
    }

    @Override
    public Class<?> getObjectType() {
        return Yellow.class;
    }

    // 是否是单例
    @Override
    public boolean isSingleton() {
        return true;
    }
}