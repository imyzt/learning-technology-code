package top.imyzt.exception.exception;

import top.imyzt.exception.enums.ErrorMsg;

/**
 * @author imyzt
 * @date 2019/5/10
 * @description 认证异常
 */
public class AuthenticationException extends AbstractDemoException {

    public AuthenticationException() {
        super(ErrorMsg.REQUEST_NOT_AUTHENTICATION);
    }
}
