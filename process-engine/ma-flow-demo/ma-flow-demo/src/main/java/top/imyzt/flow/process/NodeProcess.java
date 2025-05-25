package top.imyzt.flow.process;


import top.imyzt.flow.node.MaNode;
import top.imyzt.flow.core.Flow;

/**
 * @author imyzt
 * @date 2025/05/16
 * @description 描述信息
 */
public abstract class NodeProcess<N extends MaNode> {

    /**
     * 执行一次节点处理
     * @param node 节点
     */
    public abstract Flow process(N node, String nodeCode);

    /**
     * 节点检查
     * @param node 节点
     */
    public Flow check(N node, String verifyData) {
        return Flow.next();
    }
}
