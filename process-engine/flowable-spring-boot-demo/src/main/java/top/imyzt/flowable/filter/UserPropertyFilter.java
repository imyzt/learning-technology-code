package top.imyzt.flowable.filter;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.ExecutionListener;
import org.flowable.engine.impl.persistence.entity.ExecutionEntity;
import org.springframework.util.StringUtils;

@Slf4j
@Data
public class UserPropertyFilter implements FlowFilter, ExecutionListener {

    private String propertyName;
    private String operator;
    private String propertyValue;

    @Override
    public String getName() {
        return "用户属性过滤器";
    }

    @Override
    public boolean filter(DelegateExecution execution) {
        // 参数校验
        if (!StringUtils.hasText(propertyName) || !StringUtils.hasText(operator) || !StringUtils.hasText(propertyValue)) {
            log.error("过滤器参数不完整 - propertyName: {}, operator: {}, propertyValue: {}", 
                    propertyName, operator, propertyValue);
            terminateProcess(execution);
            return false;
        }

        String userId = (String) execution.getVariable("userId");
        log.info("用户[{}]属性过滤 - 属性[{}] {} {}", userId, propertyName, operator, propertyValue);

        // 模拟获取用户属性
        String userProperty = mockGetUserProperty(userId, propertyName);
        log.info("用户[{}]属性[{}]实际值: {}", userId, propertyName, userProperty);

        // 根据操作符进行过滤
        boolean result = false;
        switch (operator) {
            case "=":
                result = propertyValue.equals(userProperty);
                break;
            case "like":
                result = userProperty != null && userProperty.contains(propertyValue);
                break;
            case "in":
                result = userProperty != null && propertyValue.contains(userProperty);
                break;
            default:
                log.warn("不支持的操作符: {}", operator);
                result = false;
        }

        // 如果过滤不通过，终止流程
        if (!result) {
            log.info("用户[{}]属性过滤未通过，终止流程", userId);
            terminateProcess(execution);
        }

        return result;
    }

    @Override
    public void notify(DelegateExecution execution) {
        // 在流程执行时调用 filter 方法
        filter(execution);
    }

    private String mockGetUserProperty(String userId, String propertyName) {
        // 模拟获取用户属性
        if ("gender".equals(propertyName)) {
            return "男";
        } else if ("name".equals(propertyName)) {
            return "张三";
        }
        return null;
    }

    private void terminateProcess(DelegateExecution execution) {
        if (execution instanceof ExecutionEntity) {
            ExecutionEntity executionEntity = (ExecutionEntity) execution;
            // 设置流程变量，标记过滤未通过
            execution.setVariable("filterFailed", true);
            execution.setVariable("filterFailedReason", "用户属性[" + propertyName + "]不满足条件");
            // 终止流程
            executionEntity.setEnded(true);
            executionEntity.setDeleteReason("过滤器未通过");
        }
    }

}
