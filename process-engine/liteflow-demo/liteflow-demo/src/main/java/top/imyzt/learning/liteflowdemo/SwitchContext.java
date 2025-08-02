package top.imyzt.learning.liteflowdemo;


import com.yomahub.liteflow.context.ContextBean;

/**
 * @author imyzt
 * @date 2025/06/13
 * @description 描述信息
 */
@ContextBean(value = "switchValue")
public record SwitchContext (String nodeId, String tag) {

    public String next() {
        return nodeId + ":" + tag;
    }
}
