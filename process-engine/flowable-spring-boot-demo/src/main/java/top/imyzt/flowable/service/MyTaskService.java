package top.imyzt.flowable.service;


import org.flowable.engine.TaskService;
import org.flowable.task.api.Task;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author imyzt
 * @date 2025/05/05
 * @description 描述信息
 */
@Service
public class MyTaskService {

    @Resource
    private TaskService taskService;

    public List<Task> getTasks(String assignee) {
        return taskService.createTaskQuery().taskAssignee(assignee).list();
    }

    public void completeTask(String taskId) {
        taskService.complete(taskId);
    }
}
