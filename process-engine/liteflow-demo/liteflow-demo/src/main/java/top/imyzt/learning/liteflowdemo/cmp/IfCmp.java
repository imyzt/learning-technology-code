package top.imyzt.learning.liteflowdemo.cmp;

import com.yomahub.liteflow.annotation.LiteflowComponent;
import com.yomahub.liteflow.core.NodeBooleanComponent;

@LiteflowComponent("ifCmp")
public class IfCmp extends NodeBooleanComponent {

    @Override
    public boolean processBoolean() throws Exception {
        String nodeData = String.format("cmpName: %s \n tag: %s \n data: %s \n bindData: %s",
                this.getClass().getSimpleName(), this.getTag(), this.getCmpData(String.class),
                this.getBindData("nodeData", String.class));
        System.out.println(nodeData);
        return false;
    }
}
