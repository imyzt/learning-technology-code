package top.imyzt.learning.jackson.pojo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author imyzt
 * @date 2021/07/22
 * @description 参数A
 */
@EqualsAndHashCode(callSuper = true)
@Data
@JsonTypeName(value = "paramA")
public class ParamA extends BaseParam {

    /**
     * 参数A
     */
    @JsonProperty("a")
    private String a;
}