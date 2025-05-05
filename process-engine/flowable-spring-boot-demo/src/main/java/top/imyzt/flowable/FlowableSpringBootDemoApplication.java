package top.imyzt.flowable;

import org.flowable.task.api.Task;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import top.imyzt.flowable.service.MyTaskService;
import top.imyzt.flowable.service.ProcessService;

import java.util.List;

@SpringBootApplication
public class FlowableSpringBootDemoApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext applicationContext = SpringApplication.run(FlowableSpringBootDemoApplication.class, args);

        String processId = applicationContext.getBean(ProcessService.class).startProcess("imyzt");
        System.out.println(processId);

        MyTaskService taskService = applicationContext.getBean(MyTaskService.class);
        List<Task> imyztTaskList = taskService.getTasks("imyzt");
        for (Task task : imyztTaskList) {
            System.out.println(task.getName());
            taskService.completeTask(task.getId());
        }

    }



}
