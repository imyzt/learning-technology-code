package top.imyzt.valiadtor.feature.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import top.imyzt.valiadtor.feature.web.validator.EnumCode;

/**
 * @author imyzt
 * @date 2019/07/12
 * @description 性别枚举
 */
@ToString
@AllArgsConstructor
@Getter
public enum GenderEnum implements EnumCode {

    /**
     * 男
     */
    MALE(1),
    /**
     * 女
     */
    FEMALE(0),

    ;

    private Integer code;

    @Override
    public Integer getCode() {
        return this.code;
    }
}
