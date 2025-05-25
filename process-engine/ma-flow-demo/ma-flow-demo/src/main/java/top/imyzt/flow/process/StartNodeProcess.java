package top.imyzt.flow.process;


import top.imyzt.flow.node.StartNode;
import top.imyzt.flow.core.Flow;

/**
 * @author imyzt
 * @date 2025/05/16
 * @description 开始节点
 */
public class StartNodeProcess extends NodeProcess<StartNode> {
    @Override
    public Flow process(StartNode node, String nodeCode) {
        return Flow.next();
    }
}
