package top.imyzt.flow.node;


import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author imyzt
 * @date 2025/05/16
 * @description 圈人节点
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class AudiencesNode extends MaNode{

    /**
     * 圈人规则
     */
    private String rule;
}
