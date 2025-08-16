package top.imyzt.learning.liteflowdemo.cmp;


import com.yomahub.liteflow.core.NodeComponent;
import top.imyzt.learning.liteflowdemo.context.StateContext;

/**
 * @author imyzt
 * @date 2025/08/05
 * @description 基础组件
 */
public abstract class BaseCmp extends NodeComponent {

    public void customerMethod(NodeComponent cmp) {
        System.out.println("customerMethod: " + cmp.getNodeId() + ":" + cmp.getTag());
    }

    public static String getNodeUnionId(NodeComponent cmp) {
        return cmp.getNodeId() + ":" + cmp.getTag();
    }

    @Override
    public boolean isAccess() {
        StateContext contextBean = this.getContextBean(StateContext.class);
        return contextBean.isExecuted(getNodeUnionId(this));
    }

    @Override
    public void process() throws Exception {
        String nodeData = String.format("cmpName: %s \n tag: %s \n data: %s \n bindData: %s",
                this.getClass().getSimpleName(), this.getTag(), this.getCmpData(String.class),
                this.getBindData("nodeData", String.class));
        System.out.println(nodeData);
    }

    @Override
    public void onSuccess() throws Exception {
        StateContext contextBean = this.getContextBean(StateContext.class);
        contextBean.markExecuted(getNodeUnionId(this));
    }
}
