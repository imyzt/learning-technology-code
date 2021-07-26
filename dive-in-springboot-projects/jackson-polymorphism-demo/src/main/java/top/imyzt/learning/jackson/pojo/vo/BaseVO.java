package top.imyzt.learning.jackson.pojo.vo;

import lombok.Data;
import top.imyzt.learning.jackson.pojo.IParamVO;

/**
 * @author imyzt
 * @date 2021/07/22
 * @description 基础出参类
 */
@Data
public class BaseVO implements IParamVO {

    /**
     * 基础信息
     */
    private String baseInfo;
}