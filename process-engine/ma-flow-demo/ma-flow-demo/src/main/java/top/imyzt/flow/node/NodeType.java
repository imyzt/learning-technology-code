package top.imyzt.flow.node;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import top.imyzt.flow.process.ActionNodeProcess;
import top.imyzt.flow.process.EndNodeProcess;
import top.imyzt.flow.process.LazyNodeProcess;
import top.imyzt.flow.process.NodeProcess;
import top.imyzt.flow.process.StartNodeProcess;

/**
 * @author imyzt
 * @date 2025/05/19
 * @description 描述信息
 */

@Getter
@ToString
@AllArgsConstructor
public enum NodeType {

    START("开始节点", StartNodeProcess.class),
    END("结束节点", EndNodeProcess.class),
    LAZY("延迟节点", LazyNodeProcess.class),
    IF("分支节点", null),
    AUDIENCES("圈人节点", null),
    ACTION("动作节点", ActionNodeProcess.class),
    ;
    private final String desc;
    private final Class<? extends NodeProcess<? extends MaNode>> process;
}
