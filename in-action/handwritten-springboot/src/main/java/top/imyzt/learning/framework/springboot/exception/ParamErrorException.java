package top.imyzt.learning.framework.springboot.exception;

/**
 * @author imyzt
 * @date 2020/10/25
 * @description 描述信息
 */
public class ParamErrorException extends RuntimeException {

    public ParamErrorException(String message) {
        super(message);
    }
}