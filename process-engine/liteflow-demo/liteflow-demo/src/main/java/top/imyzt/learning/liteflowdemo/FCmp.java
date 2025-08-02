package top.imyzt.learning.liteflowdemo;

import com.yomahub.liteflow.annotation.LiteflowComponent;
import com.yomahub.liteflow.core.NodeComponent;

@LiteflowComponent("f")
public class FCmp extends NodeComponent {

    @Override
    public void process() {
        System.out.println("f");
    }
}
