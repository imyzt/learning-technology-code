package top.imyzt.flow.domain.entity;


import lombok.Data;
import top.imyzt.flow.node.MaRuleFusion;

import java.time.LocalDateTime;

/**
 * @author imyzt
 * @date 2025/05/16
 * @description MA 任务实体
 */
@Data
public class TaskEntity {

    /**
     * 任务ID
     */
    private Long id;

    /**
     * 任务名称
     */
    private String name;

    /**
     * 规则
     */
    private String rule;

    /**
     * 规则对象
     */
    private MaRuleFusion ruleObj;

    /**
     * 状态, 0待启用, 1-启用，2-停用
     */
    private Integer status;

    /**
     * 流程开始时间
     */
    private LocalDateTime allowTimeStart;

    /**
     * 流程结束时间
     */
    private LocalDateTime allowTimeEnd;

    /**
     * 触发事件编码
     */
    private String eventCode;

}
