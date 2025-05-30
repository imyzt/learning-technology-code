package top.imyzt.flowable.filter;

import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.delegate.DelegateExecution;

@Slf4j
public class UserLevelFilter implements FlowFilter {
    
    @Override
    public String getName() {
        return "用户等级过滤器";
    }
    
    @Override
    public boolean filter(DelegateExecution execution) {
        String userId = (String) execution.getVariable("userId");
        // 模拟获取用户等级
        int userLevel = mockGetUserLevel(userId);
        log.info("用户[{}]等级: {}", userId, userLevel);
        
        // 设置用户等级到流程变量
        execution.setVariable("userLevel", userLevel);
        
        // 用户等级大于等于2级才允许继续
        return userLevel >= 2;
    }
    
    private int mockGetUserLevel(String userId) {
        // 模拟获取用户等级，随机返回1-5
        return (int) (Math.random() * 5) + 1;
    }
} 