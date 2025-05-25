package top.imyzt.flow.node;


import lombok.Data;
import lombok.EqualsAndHashCode;
import top.imyzt.flow.action.data.PrintLogData;
import top.imyzt.flow.action.data.SendSmsData;

/**
 * @author imyzt
 * @date 2025/05/16
 * @description 描述信息
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ActionNode extends MaNode {

    /**
     * 动作编码
     */
    private String actionCode;

    /**
     * 发送短信参数
     */
    private SendSmsData sendSmsData;

    /**
     * 打印日志参数
     */
    private PrintLogData printLogData;

    // /**
    //  * 动作参数
    //  */
    // private String actionParam;
}
