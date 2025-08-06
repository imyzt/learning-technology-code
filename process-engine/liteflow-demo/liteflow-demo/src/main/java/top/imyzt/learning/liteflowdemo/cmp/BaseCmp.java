package top.imyzt.learning.liteflowdemo.cmp;


import com.yomahub.liteflow.core.NodeComponent;

/**
 * @author imyzt
 * @date 2025/08/05
 * @description 描述信息
 */
public abstract class BaseCmp extends NodeComponent {
    @Override
    public void process() throws Exception {
        String nodeData = String.format("cmpName: %s \n tag: %s \n data: %s \n bindData: %s",
                this.getClass().getSimpleName(), this.getTag(), this.getCmpData(String.class),
                this.getBindData("nodeData", String.class));
        System.out.println(nodeData);
    }
}
