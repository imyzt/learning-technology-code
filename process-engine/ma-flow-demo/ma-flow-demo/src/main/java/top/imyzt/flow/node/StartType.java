package top.imyzt.flow.node;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * @author imyzt
 * @date 2025/05/19
 * @description 描述信息
 */

@Getter
@ToString
@AllArgsConstructor
public enum StartType {

    EVENT("事件触发模式"),
    AUDIENCE("圈人触发模式"),
    ;
    private final String desc;
}
