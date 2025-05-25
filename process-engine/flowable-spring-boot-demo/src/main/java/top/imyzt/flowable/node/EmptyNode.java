package top.imyzt.flowable.node;


import top.imyzt.flowable.FlowContext;

/**
 * @author imyzt
 * @date 2025/05/24
 * @description 描述信息
 */
public class EmptyNode extends Node<Object>{
    @Override
    public String convert(FlowContext context) {
        return this.getId();
    }
}
