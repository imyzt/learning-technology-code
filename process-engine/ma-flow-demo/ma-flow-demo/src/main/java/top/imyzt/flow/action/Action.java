package top.imyzt.flow.action;


import top.imyzt.flow.node.ActionNode;
import top.imyzt.flow.core.Flow;

/**
 * @author imyzt
 * @date 2025/05/16
 * @description 描述信息
 */
public abstract class Action<T extends ActionData> {

    private static final java.util.Map<String, Action<? extends ActionData>> actions = new java.util.HashMap<>();

    protected Action() {
        actions.put(this.actionType().getCode(), this);
    }

    public abstract ActionType actionType();

    public abstract T getAttr(ActionNode node);

    public abstract void init(T attr);

    public Flow loopVerify(T attr, String verifyDate) {
        return Flow.next();
    }

    public abstract void process(T attr, String nodeCode);

    @SuppressWarnings("unchecked")
    public static Action<ActionData> getAction(String actionType) {
        return (Action<ActionData>) actions.get(actionType);
    }
}
