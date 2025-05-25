package top.imyzt.flow.action.impl;


import lombok.extern.slf4j.Slf4j;
import top.imyzt.flow.action.Action;
import top.imyzt.flow.action.ActionData;
import top.imyzt.flow.action.ActionType;
import top.imyzt.flow.action.data.PrintLogData;
import top.imyzt.flow.node.ActionNode;

/**
 * @author imyzt
 * @date 2025/05/19
 * @description 描述信息
 */
@Slf4j
public class PrintLogActionImpl extends Action<PrintLogData> {
    @Override
    public ActionType actionType() {
        return ActionType.PRINT_LOG;
    }

    @Override
    public PrintLogData getAttr(ActionNode node) {
        return node.getPrintLogData();
    }

    @Override
    public void init(PrintLogData attr) {
        log.info("PrintLogActionImpl init");
    }

    @Override
    public void process(PrintLogData attr, String nodeCode) {

    }
}
