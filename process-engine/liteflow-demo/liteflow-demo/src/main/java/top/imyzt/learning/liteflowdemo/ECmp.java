package top.imyzt.learning.liteflowdemo;

import com.yomahub.liteflow.annotation.LiteflowComponent;
import com.yomahub.liteflow.core.NodeComponent;

@LiteflowComponent("e")
public class ECmp extends NodeComponent {

    @Override
    public void process() {
        System.out.println("e");
    }
}
