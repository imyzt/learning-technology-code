package top.imyzt.learning.spring.framework.exception;

/**
 * @author imyzt
 * @date 2020/10/24
 * @description 描述信息
 */
public class BeanNameExistsException extends RuntimeException {

    private String name;

    public BeanNameExistsException(String name) {
        super(name);
        this.name = name;
    }
}