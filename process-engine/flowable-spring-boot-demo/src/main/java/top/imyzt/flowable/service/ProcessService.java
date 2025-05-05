package top.imyzt.flowable.service;


import org.flowable.engine.RuntimeService;
import org.flowable.engine.runtime.ProcessInstance;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @author imyzt
 * @date 2025/05/05
 * @description 描述信息
 */
@Service
public class ProcessService {

    @Resource
    private RuntimeService runtimeService;

    public String startProcess(String assignee) {
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("myProcess", Map.of("assignee", assignee));
        return processInstance.getId();
    }
}
