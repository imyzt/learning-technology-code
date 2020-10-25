package top.imyzt.learning.spring.framework.exception;

/**
 * @author imyzt
 * @date 2020/10/24
 * @description 描述信息
 */
public class BeanNameExistsException extends RuntimeException {

    public BeanNameExistsException(String existsBeanName) {
        super("The bean [" + existsBeanName + "] is exists");
    }
}