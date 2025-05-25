package top.imyzt.flow;


import com.alibaba.fastjson2.JSON;
import top.imyzt.flow.domain.entity.TaskEntity;
import top.imyzt.flow.node.AudiencesNode;
import top.imyzt.flow.node.EndNode;
import top.imyzt.flow.node.LazyNode;
import top.imyzt.flow.node.MaRuleFusion;
import top.imyzt.flow.node.StartNode;
import top.imyzt.flow.node.StartType;

import java.util.ArrayList;
import java.util.List;

/**
 * @author imyzt
 * @date 2025/05/17
 * @description 描述信息
 */
public class Main {

    private static final List<TaskEntity> taskList = new ArrayList<>();

    static {

        StartNode startNode = new StartNode();
        startNode.setStartType("EVENT");
        startNode.setEventCode("member_register");
        AudiencesNode audiencesNode = new AudiencesNode();
        audiencesNode.setRule("username != null");
        startNode.setAudiences(audiencesNode);
        MaRuleFusion start = new MaRuleFusion();
        start.setStartNode(startNode);

        LazyNode lazyNode = new LazyNode();
        lazyNode.setLazyType(1);
        lazyNode.setDay(1);
        lazyNode.setHour(1);
        lazyNode.setMinute(1);
        MaRuleFusion lazy = new MaRuleFusion();
        lazy.setLazyNode(lazyNode);
        start.setChild(lazy);

        EndNode endNode = new EndNode();
        MaRuleFusion end = new MaRuleFusion();
        end.setEndNode(endNode);
        lazy.setChild(end);

        String rule = JSON.toJSONString(start);

        TaskEntity te1 = new TaskEntity();
        te1.setId(1L);
        te1.setName("名称1");
        te1.setRule(rule);
        te1.setStatus(1);
        te1.setAllowTimeStart(null);
        te1.setAllowTimeEnd(null);
        te1.setEventCode("member_register");
        addTask(te1);

        // TaskEntity te2 = new TaskEntity();
        // te2.setId(2L);
        // te2.setName("名称2");
        // te2.setRule("规则2");
        // te2.setStatus(1);
        // te2.setAllowTimeStart(null);
        // te2.setAllowTimeEnd(null);
        // te2.setEventCode("auth_phone");
        // taskList.add(te1);
    }

    private static void addTask(TaskEntity t) {

        // 检查任务合法性
        MaRuleFusion ruleObj = t.getRuleObj();
        if (ruleObj.getStartNode() == null) {
            throw new IllegalArgumentException("必须有开始节点");
        }

        // 设置空编码
        MaRuleFusion.nodeCycle(ruleObj, node -> {
            node.setNodeCode(null);
        });

        // 特殊处理开始节点
        StartNode startNode = ruleObj.getStartNode();
        if (StartType.AUDIENCE.name().equals(startNode.getStartType())) {
            // 圈人节点
            AudiencesNode audiencesNode = startNode.getAudiences();
            if (audiencesNode == null) {
                throw new IllegalArgumentException("必须有圈人节点");
            }
        } else {
            // 事件节点
            String eventNode = startNode.getEventCode();
            if (eventNode == null) {
                throw new IllegalArgumentException("必须有事件编码");
            }
        }





        taskList.add(t);
    }

    public static void main(String[] args) {

        taskList.stream().filter(t -> {
            String eventCode = "member_register";
            return t.getEventCode().equals(eventCode);
        }).forEach(task -> {

            task.ge

        });
    }
}
