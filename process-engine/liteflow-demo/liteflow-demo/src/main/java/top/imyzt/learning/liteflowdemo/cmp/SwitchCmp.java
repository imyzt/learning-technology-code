package top.imyzt.learning.liteflowdemo.cmp;

import com.yomahub.liteflow.annotation.LiteflowComponent;
import com.yomahub.liteflow.core.NodeComponent;
import top.imyzt.learning.liteflowdemo.decl.ISwitch;

@LiteflowComponent("switchCmp")
public class SwitchCmp implements ISwitch {

    @Override
    public String process(NodeComponent bindCmp) {
        String nodeData = String.format("cmpName: %s \n tag: %s \n data: %s \n bindData: %s",
                this.getClass().getSimpleName(), bindCmp.getTag(), bindCmp.getCmpData(String.class),
                bindCmp.getBindData("nodeData", String.class));
        System.out.println(nodeData);
        return bindCmp.getCmpData(String.class);
    }
}
