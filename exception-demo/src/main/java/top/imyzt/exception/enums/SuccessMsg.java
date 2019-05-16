package top.imyzt.exception.enums;

import lombok.Getter;

/**
 * @author imyzt
 * @date 2019/5/8
 * @description 成功信息枚举类
 */
@Getter
public enum  SuccessMsg {

    /**
     * 操作成功
     */
    SUCCESS(200, "操作成功"),
    ;

    private Integer code;
    private String message;

    SuccessMsg(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
