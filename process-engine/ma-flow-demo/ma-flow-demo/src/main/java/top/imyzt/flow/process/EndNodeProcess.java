package top.imyzt.flow.process;


import top.imyzt.flow.core.Flow;
import top.imyzt.flow.node.EndNode;

/**
 * @author imyzt
 * @date 2025/05/16
 * @description 开始节点
 */
public class EndNodeProcess extends NodeProcess<EndNode> {
    @Override
    public Flow process(EndNode node, String nodeCode) {
        return Flow.end();
    }
}
