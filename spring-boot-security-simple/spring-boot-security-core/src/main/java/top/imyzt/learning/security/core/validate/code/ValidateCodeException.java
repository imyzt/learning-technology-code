package top.imyzt.learning.security.core.validate.code;

import org.springframework.security.core.AuthenticationException;

/**
 * @author imyzt
 * @date 2019/6/10
 * @description 验证码异常的异常实现
 */
public class ValidateCodeException extends AuthenticationException {

    public ValidateCodeException(String msg) {
        super(msg);
    }
}
