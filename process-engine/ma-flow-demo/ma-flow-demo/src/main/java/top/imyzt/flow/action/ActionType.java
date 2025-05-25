package top.imyzt.flow.action;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * @author imyzt
 * @date 2025/05/16
 * @description 描述信息
 */

@Getter
@ToString
@AllArgsConstructor
public enum ActionType {

    PRINT_LOG("print_log", "打印日志"),
    SEND_SMS("send_sms", "发送短信"),

    ;
    private final String code;
    private final String name;
}
