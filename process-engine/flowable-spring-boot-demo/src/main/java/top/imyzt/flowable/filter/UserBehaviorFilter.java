package top.imyzt.flowable.filter;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.ExecutionListener;
import org.flowable.engine.impl.persistence.entity.ExecutionEntity;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Slf4j
@Data
public class UserBehaviorFilter implements FlowFilter, ExecutionListener {
    
    private String behaviorType;
    private String targetId;
    private String timeRange;
    private Integer count;
    
    @Override
    public String getName() {
        return "用户行为过滤器";
    }
    
    @Override
    public boolean filter(DelegateExecution execution) {
        // 参数校验
        if (!StringUtils.hasText(behaviorType) || !StringUtils.hasText(targetId) || 
            !StringUtils.hasText(timeRange) || count == null || count <= 0) {
            log.error("过滤器参数不完整 - behaviorType: {}, targetId: {}, timeRange: {}, count: {}", 
                    behaviorType, targetId, timeRange, count);
            terminateProcess(execution);
            return false;
        }

        String userId = (String) execution.getVariable("userId");
        log.info("用户[{}]行为过滤 - 行为[{}] 目标[{}] 时间范围[{}] 次数[{}]", 
                userId, behaviorType, targetId, timeRange, count);
        
        // 模拟获取用户行为次数
        int behaviorCount = mockGetUserBehaviorCount(userId, behaviorType, targetId, timeRange);
        log.info("用户[{}]行为[{}]实际次数: {}", userId, behaviorType, behaviorCount);
        
        boolean result = behaviorCount >= count;
        
        // 如果过滤不通过，终止流程
        if (!result) {
            log.info("用户[{}]行为过滤未通过，终止流程", userId);
            terminateProcess(execution);
        }
        
        return result;
    }

    @Override
    public void notify(DelegateExecution execution) {
        // 在流程执行时调用 filter 方法
        filter(execution);
    }
    
    private int mockGetUserBehaviorCount(String userId, String behaviorType, String targetId, String timeRange) {
        // 模拟获取用户行为次数
        if ("view".equals(behaviorType) && "product_001".equals(targetId)) {
            return 5; // 模拟用户查看了5次商品
        }
        return 0;
    }
    
    private void terminateProcess(DelegateExecution execution) {
        if (execution instanceof ExecutionEntity) {
            ExecutionEntity executionEntity = (ExecutionEntity) execution;
            // 设置流程变量，标记过滤未通过
            execution.setVariable("filterFailed", true);
            execution.setVariable("filterFailedReason", "用户行为[" + behaviorType + "]不满足条件");
            // 终止流程
            executionEntity.setEnded(true);
            executionEntity.setDeleteReason("过滤器未通过");
        }
    }
} 