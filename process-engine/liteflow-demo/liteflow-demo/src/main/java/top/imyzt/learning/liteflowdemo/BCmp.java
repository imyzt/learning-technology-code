package top.imyzt.learning.liteflowdemo;

import com.yomahub.liteflow.annotation.LiteflowComponent;
import com.yomahub.liteflow.core.NodeComponent;
import com.yomahub.liteflow.core.NodeSwitchComponent;
import org.springframework.stereotype.Component;

@LiteflowComponent("b")
public class BCmp extends NodeSwitchComponent {

    @Override
    public String processSwitch() throws Exception {
        // 获取上下文
        return this.getContextBean(SwitchContext.class).getSwitchValue();
    }
}
