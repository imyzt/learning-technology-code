package top.imyzt.learning.liteflowdemo;

import com.yomahub.liteflow.core.FlowExecutor;
import com.yomahub.liteflow.flow.LiteflowResponse;
import jakarta.annotation.Resource;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

@SpringBootApplication
public class LiteflowDemoApplication {

    @Resource
    private FlowExecutor flowExecutor;

    public static void main(String[] args) {
        SpringApplication.run(LiteflowDemoApplication.class, args);
    }

    @EventListener(ApplicationReadyEvent.class)
    public void init() {
        StateContext stateContext = new StateContext();

        SwitchContext context = new SwitchContext("c", "t2");
        LiteflowResponse response = flowExecutor.execute2Resp("chain1", "arg", context, stateContext);
        System.out.println(response);
    }
}
