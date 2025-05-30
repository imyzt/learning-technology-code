package top.imyzt.flowable.service;


import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.flowable.common.engine.api.delegate.Expression;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;

/**
 * @author imyzt
 * @date 2025/05/24
 * @description 描述信息
 */
@Setter
@Slf4j
public class MyDelegate implements JavaDelegate {

    private Expression source;

    @Override
    public void execute(DelegateExecution delegateExecution) {

        // String source = (String) delegateExecution.getVariable("source");

        ;
        if (source == null) {
            log.info("delegateDemo..., source is null");
            return;
        }
        log.info("delegateDemo..., source={}", source.getValue(delegateExecution));
    }
}
