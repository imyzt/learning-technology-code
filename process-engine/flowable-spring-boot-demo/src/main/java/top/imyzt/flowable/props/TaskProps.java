package top.imyzt.flowable.props;


import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author imyzt
 * @date 2025/05/25
 * @description 描述信息
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class TaskProps extends Props {

    private Listener delegate;
}
