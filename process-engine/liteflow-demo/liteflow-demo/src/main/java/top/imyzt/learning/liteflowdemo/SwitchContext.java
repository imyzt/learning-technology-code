package top.imyzt.learning.liteflowdemo;


import com.yomahub.liteflow.context.ContextBean;

/**
 * @author imyzt
 * @date 2025/06/13
 * @description 描述信息
 */
@ContextBean(value = "switchValue")
public class SwitchContext {
    private String switchValue;

    public String getSwitchValue() {
        return switchValue;
    }

    public void setSwitchValue(String switchValue) {
        this.switchValue = switchValue;
    }
}
