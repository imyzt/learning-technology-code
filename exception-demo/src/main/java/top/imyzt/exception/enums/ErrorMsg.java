package top.imyzt.exception.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * @author imyzt
 * @date 2019/5/8
 * @description 错误信息枚举类
 */
@Getter
@ToString
@AllArgsConstructor
public enum  ErrorMsg {

    /**
     * 一般情况下禁止使用. 由全局异常拦截器无法识别的异常时使用
     */
    ERROR(500, "服务器错误"),

    /*  ---------------------------- 500100-500199 代表业务类异常  ---------------------------- */

    /**
     * 用户名密码错误
     */
    USERNAME_PASSWORD_ERROR(500101, "用户名密码错误"),
    /**
     * 需要登录
     */
    NEED_LOGIN(500102, "需要登录"),


    OBJECT_UNDEFINED(500103, "对象未定义"),

    /**
     * 未认证
     */
    REQUEST_NOT_AUTHENTICATION(500104, "请求未认证"),

    /*  ---------------------------- 500200-500299 代表IO类异常  ---------------------------- */
    /**
     * 文件未找到
     */
    FILE_NOT_FOUND(500201, "文件未找到"),


    /* ---------------------------- 500300-500399 代表HTTP类异常  ---------------------------- */
    /**
     * 不支持的请求方法
     */
    UNSUPPORTED_REQUEST_METHOD(500301, "不支持的请求方法"),
    /**
     * 错误的请求参数
     */
    MISSING_REQUEST_PARAMETER(500302, "错误的请求参数"),

    ;

    private Integer code;
    private String message;

    public void setMessage(String message) {
        this.message = message;
    }
}
