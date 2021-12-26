package top.imyzt.learning.plugin.idea.codegen.domain.service;

import com.intellij.openapi.project.Project;
import top.imyzt.learning.plugin.idea.codegen.domain.model.vo.ProjectConfig;

/**
 * @author imyzt
 * @date 2021/12/26
 * @description 项目生成类
 */
public interface IProjectGenerator {

    void doGenerator(Project project, String entryPath, ProjectConfig projectConfig);
}