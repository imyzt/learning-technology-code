package top.imyzt.learning.liteflowdemo;

import com.yomahub.liteflow.annotation.LiteflowComponent;
import com.yomahub.liteflow.core.NodeComponent;
import org.springframework.stereotype.Component;

@LiteflowComponent("d")
public class DCmp extends NodeComponent {

    @Override
    public void process() {
        System.out.println("d");
    }
}
