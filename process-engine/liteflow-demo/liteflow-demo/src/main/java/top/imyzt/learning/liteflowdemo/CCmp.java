package top.imyzt.learning.liteflowdemo;

import com.yomahub.liteflow.annotation.LiteflowComponent;
import com.yomahub.liteflow.core.NodeComponent;

@LiteflowComponent("c")
public class CCmp extends NodeComponent {

    @Override
    public void process() {
        String tag = this.getTag();
        System.out.println("c:" + tag);
    }
}
