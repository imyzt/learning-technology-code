package top.imyzt.learning.liteflowdemo.cmp;

import com.yomahub.liteflow.annotation.LiteflowComponent;
import com.yomahub.liteflow.core.NodeComponent;
import top.imyzt.learning.liteflowdemo.DelayedTask;
import top.imyzt.learning.liteflowdemo.LiteflowDemoApplication;
import top.imyzt.learning.liteflowdemo.context.StateContext;

@LiteflowComponent("delayCmp")
public class DelayCmp extends NodeComponent {

    @Override
    public boolean isAccess() {
        StateContext contextBean = this.getContextBean(StateContext.class);
        return contextBean.isExecuted(BaseCmp.getNodeUnionId(this));
    }

    @Override
    public void onSuccess() throws Exception {
        StateContext contextBean = this.getContextBean(StateContext.class);
        contextBean.markExecuted(BaseCmp.getNodeUnionId(this));
    }

    @Override
    public void process() throws Exception {
        String nodeData = String.format("cmpName: %s \n tag: %s \n data: %s \n bindData: %s",
                this.getClass().getSimpleName(), this.getTag(), this.getCmpData(String.class),
                this.getBindData("nodeData", String.class));
        System.out.println(nodeData);

        // 模拟延迟
        LiteflowDemoApplication.addTask(new DelayedTask("任务3", 1000));

        this.setIsEnd(true);
    }
}
