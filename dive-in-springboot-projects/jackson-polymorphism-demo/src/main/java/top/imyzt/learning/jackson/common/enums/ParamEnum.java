package top.imyzt.learning.jackson.common.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import top.imyzt.learning.jackson.handler.HandlerA;
import top.imyzt.learning.jackson.handler.HandlerB;

/**
 * @author imyzt
 * @date 2021/07/22
 * @description 参数类型策略枚举
 */
@Getter
@ToString
@AllArgsConstructor
public enum ParamEnum {

    /**
     * 参数A对应处理器
     */
    A("paramA", HandlerA.class),
    /**
     * 参数B对应处理器
     */
    B("paramB", HandlerB.class),

    ;

    @JsonValue
    private String type;

    private Class<?> handlerClazz;
}
