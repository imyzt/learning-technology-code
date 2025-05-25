package top.imyzt.flow.process;


import lombok.extern.slf4j.Slf4j;
import top.imyzt.flow.action.Action;
import top.imyzt.flow.action.ActionData;
import top.imyzt.flow.node.ActionNode;
import top.imyzt.flow.core.Flow;

/**
 * @author imyzt
 * @date 2025/05/16
 * @description 动作节点
 */
@Slf4j
public class ActionNodeProcess extends NodeProcess<ActionNode> {
    @Override
    public Flow process(ActionNode node, String nodeCode) {

        Action<ActionData> action = Action.getAction(node.getActionCode());
        if (null == action) {
            log.error("动作已下线:{}", node.getActionCode());
            return Flow.next();
        } else if (null != action.getAttr(node).getInitConfigStatus() && !action.getAttr(node).getInitConfigStatus()) {
            log.error("节点初始化失败{}[{}]", node.getActionCode(), action.actionType().getName());
            return Flow.next();
        }

        action.process(action.getAttr(node), nodeCode);

        return Flow.next();
    }

    @Override
    public Flow check(ActionNode node, String verifyData) {
        Action<ActionData> action = Action.getAction(node.getActionCode());
        if (action == null) {
            log.error("未找到动作：{}", node.getActionCode());
            return Flow.next();
        }
        return action.loopVerify(action.getAttr(node), verifyData);
    }
}
