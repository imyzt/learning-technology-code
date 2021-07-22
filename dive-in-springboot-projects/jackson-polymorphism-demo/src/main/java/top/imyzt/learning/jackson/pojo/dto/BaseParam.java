package top.imyzt.learning.jackson.pojo.dto;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Data;

/**
 * @author imyzt
 * @date 2021/07/22
 * @description 基础参数类
 */
@Data
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type"
)
public class BaseParam {

    /**
     * 基础信息
     */
    private String baseInfo;
}