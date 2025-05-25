package top.imyzt.flow.node;


import lombok.Data;

import java.util.List;
import java.util.function.Consumer;

/**
 * @author imyzt
 * @date 2025/05/16
 * @description 聚合节点
 */
@Data
public class MaRuleFusion {

    /**
     * 节点编码
     */
    private String nodeCode;

    /**
     * 节点中文名称
     */
    private String nodeName;

    /**
     * 节点类型
     */
    private String nodeType;

    /**
     * 子节点
     */
    private MaRuleFusion child;

    /**
     * 开始节点
     */
    private StartNode startNode;

    /**
     * 延迟节点
     */
    private LazyNode lazyNode;

    /**
     * 动作节点
     * 执行任务下发
     */
    private ActionNode actionNode;

    /**
     * 分支节点
     */
    private List<IfNode> ifNodes;

    /**
     * 最后一个节点
     */
    private EndNode endNode;

    /**
     * 上级节点编码
     */
    private String previous;

    //分支节点的子节点存储分支节点的code便于寻址
    private String parentIfCode;


    public static void nodeCycle(MaRuleFusion origin, Consumer<MaRuleFusion> consumer) {
        if (null == origin) {
            return;
        }
        consumer.accept(origin);
        if (NodeType.IF.name().equals(origin.getNodeType())) {
            for (IfNode branchNode : origin.getIfNodes()) {
                nodeCycle(branchNode, (node) -> {
                    node.setParentIfCode(origin.getNodeCode());
                    consumer.accept(node);
                });
            }
        }
        if (origin.getChild() != null) {
            nodeCycle(origin.getChild(), consumer);
        }
    }
}
