package top.imyzt.exception.exception;

import top.imyzt.exception.enums.ErrorMsg;

/**
 * @author imyzt
 * @date 2019/5/10
 * @description 参数错误异常
 */
public class ParameterException extends AbstractDemoException {

    public ParameterException(String errorField) {
        super(getErrorMsg(errorField));
    }

    private static ErrorMsg getErrorMsg(String errorField) {
        ErrorMsg errorMsg = ErrorMsg.MISSING_REQUEST_PARAMETER;
        errorMsg.setMessage(errorField);
        return errorMsg;
    }
}
