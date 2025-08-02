package top.imyzt.learning.liteflowdemo;

import com.yomahub.liteflow.annotation.LiteflowComponent;
import com.yomahub.liteflow.core.NodeComponent;
import top.imyzt.learning.liteflowdemo.decl.ISwitch;

@LiteflowComponent("b")
public class BCmp implements ISwitch {

    @Override
    public String process(NodeComponent bindCmp) {
        System.out.println("b");
        SwitchContext contextBean = bindCmp.getContextBean(SwitchContext.class);
        return contextBean.next();
    }
}
