package top.imyzt.flowable.filter;

import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.delegate.DelegateExecution;

@Slf4j
public class ProductStockFilter implements FlowFilter {
    
    @Override
    public String getName() {
        return "商品库存过滤器";
    }
    
    @Override
    public boolean filter(DelegateExecution execution) {
        String productId = (String) execution.getVariable("productId");
        // 模拟获取商品库存
        int stock = mockGetProductStock(productId);
        log.info("商品[{}]库存: {}", productId, stock);
        
        // 设置商品库存到流程变量
        execution.setVariable("productStock", stock);
        
        // 库存大于0才允许继续
        return stock > 0;
    }
    
    private int mockGetProductStock(String productId) {
        // 模拟获取商品库存，随机返回0-10
        return (int) (Math.random() * 11);
    }
} 