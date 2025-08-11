package top.imyzt.learning.liteflowdemo.cmp;

import com.yomahub.liteflow.annotation.LiteflowComponent;
import com.yomahub.liteflow.core.NodeComponent;
import top.imyzt.learning.liteflowdemo.decl.ISwitch;

@LiteflowComponent("switchCmp")
public class SwitchCmp implements ISwitch {

    @Override
    public String process(NodeComponent bindCmp) {
        System.out.println("-".repeat(10) + "switchCmp");
        // return ":" + bindCmp.getCmpData(String.class);
        return bindCmp.getCmpData(String.class);
    }
}
