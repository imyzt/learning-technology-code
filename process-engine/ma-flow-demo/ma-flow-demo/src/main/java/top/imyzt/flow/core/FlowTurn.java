package top.imyzt.flow.core;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * @author imyzt
 * @date 2025/05/16
 * @description 流程方向
 */

@Getter
@ToString
@AllArgsConstructor
public enum FlowTurn {

    NEXT("前进"),
    ERROR("异常"),
    WAIT_VERIFY("执行校验逻辑"),
    RETRY("重入当前节点"),
    END("结束");

    private final String desc;
}
