package top.imyzt.learning.plugin.idea.codegen.infrastructure;

import lombok.Data;
import top.imyzt.learning.plugin.idea.codegen.domain.model.vo.ProjectConfig;

/**
 * @author imyzt
 * @date 2021/12/26
 * @description 描述信息
 */
@Data
public class DataState {

    private ProjectConfig projectConfig = new ProjectConfig();

}