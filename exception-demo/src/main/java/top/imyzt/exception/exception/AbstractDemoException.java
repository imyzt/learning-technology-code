package top.imyzt.exception.exception;

import top.imyzt.exception.enums.ErrorMsg;
import lombok.Getter;

/**
 * @author imyzt
 * @date 2019/5/8
 * @description 异常类的抽象父类.
 */
@Getter
public abstract class AbstractDemoException extends RuntimeException {

    private ErrorMsg errorMsg;

    AbstractDemoException(ErrorMsg errorMsg, Throwable cause) {
        super(errorMsg.getMessage(), cause);
        this.errorMsg = errorMsg;
    }

    AbstractDemoException(ErrorMsg errorMsg) {
        super(errorMsg.getMessage());
        this.errorMsg = errorMsg;
    }
}
