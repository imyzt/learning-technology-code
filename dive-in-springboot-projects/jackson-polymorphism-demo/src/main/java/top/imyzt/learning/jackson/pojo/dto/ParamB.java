package top.imyzt.learning.jackson.pojo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author imyzt
 * @date 2021/07/22
 * @description 参数B
 */
@EqualsAndHashCode(callSuper = true)
@Data
@JsonTypeName(value = "paramB")
@ToString(callSuper = true)
public class ParamB extends BaseParam {

    /**
     * 参数A
     */
    @JsonProperty("b")
    private String b;
}