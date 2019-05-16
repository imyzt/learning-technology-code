package top.imyzt.exception.vo;

import top.imyzt.exception.enums.ErrorMsg;
import top.imyzt.exception.enums.SuccessMsg;
import lombok.Getter;

/**
 * @author imyzt
 * @date 2019/5/8
 * @description 服务器响应对象. 不能单独创建. 只能通过提供的静态方法创建. <br/>
 * 使用必须传递 {@link ErrorMsg }或 {@link SuccessMsg } 对象
 */
@Getter
public class Response<T> {

    private Integer code;
    private String message;
    private T data;

    private Response () {}

    private static class StaticClassSingletonHolder {
        private static final Response INSTANCE = new Response();
    }

    private static Response instance = StaticClassSingletonHolder.INSTANCE;

    public static Response success() {
        instance.code = SuccessMsg.SUCCESS.getCode();
        instance.message = SuccessMsg.SUCCESS.getMessage();
        return instance;
    }

    public static Response success(SuccessMsg successMsg) {
        instance.code = successMsg.getCode();
        instance.message = successMsg.getMessage();
        return instance;
    }

    public static <T> Response success(T data) {
        instance.code = SuccessMsg.SUCCESS.getCode();
        instance.message = SuccessMsg.SUCCESS.getMessage();
        instance.data = data;
        return instance;
    }

    public static <T> Response success(SuccessMsg successMsg, T data) {
        instance.code = successMsg.getCode();
        instance.message = successMsg.getMessage();
        instance.data = data;
        return instance;
    }

    public static Response error(ErrorMsg errorMsg) {
        instance.code = errorMsg.getCode();
        instance.message = errorMsg.getMessage();
        return instance;
    }
}
