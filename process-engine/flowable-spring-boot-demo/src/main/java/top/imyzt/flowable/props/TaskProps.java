package top.imyzt.flowable.props;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author imyzt
 * @date 2025/05/25
 * @description 任务节点属性
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class TaskProps extends Props {

    private Listener delegate;
    
    // 优惠券相关属性
    private String couponType;
    private String couponValue;
    private String couponUnit;
}
