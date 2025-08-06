package top.imyzt.learning.liteflowdemo.decl;

import com.yomahub.liteflow.annotation.LiteflowMethod;
import com.yomahub.liteflow.core.NodeComponent;
import com.yomahub.liteflow.core.proxy.LiteFlowProxyUtil;
import com.yomahub.liteflow.enums.LiteFlowMethodEnum;
import com.yomahub.liteflow.enums.NodeTypeEnum;
import com.yomahub.liteflow.slot.DataBus;
import top.imyzt.learning.liteflowdemo.context.StateContext;

/**
 * @author imyzt
 * @date 2025/07/31
 * @description 复用switch
 */
public interface ISwitch {

    @LiteflowMethod(value = LiteFlowMethodEnum.PROCESS_SWITCH, nodeType = NodeTypeEnum.SWITCH)
    default String processComponent(NodeComponent bindCmp) {
        String switchResult = process(bindCmp);
        StateContext context = bindCmp.getContextBean(StateContext.class);
        String nodeId = bindCmp.getNodeId() + ":" + bindCmp.getTag();
        context.saveDecision(nodeId, switchResult);
        return switchResult;
    }

    String process(NodeComponent bindCmp);

    @LiteflowMethod(value = LiteFlowMethodEnum.IS_ACCESS, nodeType = NodeTypeEnum.SWITCH)
    default boolean isAccess(NodeComponent bindCmp) {
        StateContext context = bindCmp.getContextBean(StateContext.class);
        String nodeId = bindCmp.getNodeId() + ":" + bindCmp.getTag();
        return !context.hasDecision(nodeId);
    }

    @LiteflowMethod(value = LiteFlowMethodEnum.ON_SUCCESS, nodeType = NodeTypeEnum.SWITCH)
    default void onSuccess(NodeComponent bindCmp) {
        StateContext context = bindCmp.getContextBean(StateContext.class);
        String switchResult =
                DataBus.getSlot(bindCmp.getSlotIndex()).getSwitchResult(getMetaValueKey(bindCmp));
        String nodeId = bindCmp.getNodeId() + ":" + bindCmp.getTag();
        context.saveDecision(nodeId, switchResult);
        // log.info("存储决策，节点ID：{}, 决策值：{}", nodeId, switchResult);
    }

    private String getMetaValueKey(NodeComponent bindCmp) {
        Class<?> originalClass = LiteFlowProxyUtil.getUserClass(bindCmp.getClass());
        return originalClass.getName();
    }

}

