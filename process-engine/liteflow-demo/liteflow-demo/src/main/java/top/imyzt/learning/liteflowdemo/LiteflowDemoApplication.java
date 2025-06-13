package top.imyzt.learning.liteflowdemo;

import com.yomahub.liteflow.core.FlowExecutor;
import com.yomahub.liteflow.flow.LiteflowResponse;
import com.yomahub.liteflow.slot.DefaultContext;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
public class LiteflowDemoApplication {

    @Resource
    private FlowExecutor flowExecutor;

    public static void main(String[] args) {
        SpringApplication.run(LiteflowDemoApplication.class, args);
    }

    @PostConstruct
    public void init() {
        SwitchContext context = new SwitchContext();
        context.setSwitchValue("t2");
        LiteflowResponse response = flowExecutor.execute2Resp("chain1", "arg", context);
        System.out.println(response);
    }
}
