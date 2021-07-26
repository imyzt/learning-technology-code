package top.imyzt.learning.jackson.pojo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Data;
import top.imyzt.learning.jackson.common.enums.ParamEnum;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * @author imyzt
 * @date 2021/07/22
 * @description 参数对象
 */
@Data
public class ParamDTO {

    @JsonProperty("type")
    @NotNull(message = "type不能为空")
    private ParamEnum type;

    @JsonProperty("param")
    @Valid
    @JsonTypeInfo(
            // 必选, 使用哪一种特征识别码
            use = JsonTypeInfo.Id.NAME,
            include = JsonTypeInfo.As.EXTERNAL_PROPERTY,
            property = "type",
            visible = true,
            defaultImpl = BaseParam.class
    )
    private BaseParam param;
}