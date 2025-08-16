package top.imyzt.learning.liteflowdemo.cmp.decl;


import com.yomahub.liteflow.annotation.LiteflowMethod;
import com.yomahub.liteflow.core.NodeComponent;
import com.yomahub.liteflow.enums.LiteFlowMethodEnum;
import com.yomahub.liteflow.enums.NodeTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author imyzt
 * @date 2025/08/16
 * @description 描述信息
 */
@Component("declCmp")
@Slf4j
public class DeclCmp extends BaseDecl {

    @LiteflowMethod(value = LiteFlowMethodEnum.PROCESS, nodeType = NodeTypeEnum.COMMON)
    public void process(NodeComponent cmp) {
        log.info("Decl.process: {}:{}", cmp.getNodeId(), cmp.getTag());
    }
}

@Slf4j
abstract class BaseDecl {

    public void customerMethod(NodeComponent cmp) {
        log.info("BaseDecl.customerMethod: {}:{}", cmp.getNodeId(), cmp.getTag());
    }

}
