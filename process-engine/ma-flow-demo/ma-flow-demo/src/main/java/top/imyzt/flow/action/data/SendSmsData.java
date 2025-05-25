package top.imyzt.flow.action.data;


import lombok.Data;
import lombok.EqualsAndHashCode;
import top.imyzt.flow.action.ActionData;

/**
 * @author imyzt
 * @date 2025/05/19
 * @description 描述信息
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SendSmsData extends ActionData {

    private String phone;

    private String content;
}
