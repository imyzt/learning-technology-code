package top.imyzt.learning.plugin.idea.tooltab.model.aggregates;

import lombok.Data;
import top.imyzt.learning.plugin.idea.tooltab.model.vo.Stock;

/**
 * @author imyzt
 * @date 2021/11/30
 * @description 聚合接口返回对象
 */
@Data
public class StockResult {

    private Integer resultcode;

    private String reason;

    private Stock[] result;
}