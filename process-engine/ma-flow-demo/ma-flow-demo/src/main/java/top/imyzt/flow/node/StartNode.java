package top.imyzt.flow.node;


import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author imyzt
 * @date 2025/05/16
 * @description 开始阶段
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class StartNode extends MaNode {

    /**
     * 开始类型
     * EVENT-事件触发
     * TIME-定时触发
     */
    private String startType;

    /**
     * 事件编码
     */
    private String eventCode;

    /**
     * 定时触发表达式
     */
    private String cron;

    /**
     * 生效开始时间
     */
    private String allowTimeStart;

    /**
     * 生效结束时间
     */
    private String allowTimeEnd;

    /**
     * 圈人节点
     */
    private AudiencesNode audiences;



}
