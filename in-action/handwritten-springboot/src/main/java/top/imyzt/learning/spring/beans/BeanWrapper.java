package top.imyzt.learning.spring.beans;

/**
 * @author imyzt
 * @date 2020/10/25
 * @description 容器保存的beanWrapper对象
 */
public class BeanWrapper {

    private Object wrapperInstance;

    private Class<?> wrapperInstanceClazz;

    public BeanWrapper(Object instance) {
        this.wrapperInstance = instance;
        this.wrapperInstanceClazz = instance.getClass();
    }

    public Object getWrapperInstance() {
        return wrapperInstance;
    }

    public Class<?> getWrapperInstanceClazz() {
        return wrapperInstanceClazz;
    }
}