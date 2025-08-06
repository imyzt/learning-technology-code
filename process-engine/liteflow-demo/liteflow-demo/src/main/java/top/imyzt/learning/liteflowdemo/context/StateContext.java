package top.imyzt.learning.liteflowdemo.context;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author imyzt
 * @date 2025/08/02
 * @description 状态上下文
 */
public class StateContext {

  /** 已执行的cmpId */
  private List<String> cmpIdList = new ArrayList<>();

  /** 决策结果 */
  private Map<String, String> decisionMap = new HashMap<>();

  /** 是否已执行 */
  public boolean isExecuted(String cmpId) {
    return !this.cmpIdList.contains(cmpId);
  }

  /** 标记已执行 */
  public void markExecuted(String cmpId) {
    if (this.cmpIdList.contains(cmpId)) {
      return;
    }
    this.cmpIdList.add(cmpId);
  }

  /** 判断是否存在决策 */
  public boolean hasDecision(String cmpId) {
    return this.cmpIdList.contains(cmpId);
  }

  /** 获取决策 */
  public String getDecision(String cmpId) {
    return this.decisionMap.get(cmpId);
  }

  /** 保存决策 */
  public void saveDecision(String nodeId, String decision) {
    this.markExecuted(nodeId);
    this.decisionMap.put(nodeId, decision);
  }
}
