package top.imyzt.learning.jackson.pojo.dto;

import lombok.Data;
import top.imyzt.learning.jackson.pojo.IParamVO;

/**
 * @author imyzt
 * @date 2021/07/22
 * @description 基础参数类
 */
@Data
public class BaseParam implements IParamVO {

    /**
     * 基础信息
     */
    private String baseInfo;
}