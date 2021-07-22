package top.imyzt.learning.jackson.pojo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Data;
import top.imyzt.learning.jackson.common.enums.ParamEnum;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

/**
 * @author imyzt
 * @date 2021/07/22
 * @description 参数对象
 */
@Data
public class ParamDTO<T extends BaseParam> {

    @JsonProperty("type")
    @NotBlank(message = "type不能为空")
    private ParamEnum type;


    @JsonProperty("param")
    @Valid
    private T param;
}