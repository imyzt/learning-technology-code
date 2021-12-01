package top.imyzt.learning.plugin.idea.tooltab.model.vo;

/**
 * @author imyzt
 * @date 2021/11/30
 * @description 聚合接口数据对象
 */
@lombok.Data
public class Data {

    /** 股票ID */
    private String gid;
    /** 股票名称 */
    private String name;
    /** 当前价格 */
    private String nowPri;
    /** 涨跌 */
    private String increase;
    /** 涨幅 */
    private String increPer;
}