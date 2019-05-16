package top.imyzt.exception.exception;

import top.imyzt.exception.enums.ErrorMsg;

/**
 * @author imyzt
 * @date 2019/5/8
 * @description 业务异常
 */
public class BusinessException extends AbstractDemoException {

    public BusinessException(ErrorMsg errorMsg, Throwable cause) {
        super(errorMsg, cause);
    }

    public BusinessException(ErrorMsg errorMsg) {
        super(errorMsg);
    }
}
