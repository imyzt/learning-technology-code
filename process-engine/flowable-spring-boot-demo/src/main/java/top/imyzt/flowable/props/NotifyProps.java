package top.imyzt.flowable.props;


import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author imyzt
 * @date 2025/05/24
 * @description 描述信息
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class NotifyProps extends Props {

    private String notifyType;

    private List<String> userList;
}
