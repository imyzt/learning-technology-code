package top.imyzt.flow.action.impl;


import lombok.extern.slf4j.Slf4j;
import top.imyzt.flow.action.Action;
import top.imyzt.flow.action.ActionData;
import top.imyzt.flow.action.ActionType;
import top.imyzt.flow.action.data.SendSmsData;
import top.imyzt.flow.node.ActionNode;

import static top.imyzt.flow.action.ActionType.SEND_SMS;

/**
 * @author imyzt
 * @date 2025/05/19
 * @description 描述信息
 */
@Slf4j
public class SendSmsActionImpl extends Action<SendSmsData> {
    @Override
    public ActionType actionType() {
        return SEND_SMS;
    }

    @Override
    public SendSmsData getAttr(ActionNode node) {
        return node.getSendSmsData();
    }

    @Override
    public void init(SendSmsData attr) {
        log.info("SendSmsActionImpl init");
    }

    @Override
    public void process(SendSmsData attr, String nodeCode) {

    }
}
