package top.imyzt.learning.jackson.handler;

import top.imyzt.learning.jackson.pojo.dto.BaseParam;
import top.imyzt.learning.jackson.pojo.vo.BaseVO;

/**
 * @author imyzt
 * @date 2021/07/22
 * @description 顶级处理器接口
 */
public interface IHandler {

    /**
     * 处理方法
     * @param param 参数
     * @return 处理结果
     */
    String handler(BaseParam param);

    /**
     * 处理方法
     * @return 处理结果
     */
    BaseVO getData();

}
