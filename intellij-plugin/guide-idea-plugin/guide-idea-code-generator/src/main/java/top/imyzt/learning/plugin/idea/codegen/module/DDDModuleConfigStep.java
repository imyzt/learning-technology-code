package top.imyzt.learning.plugin.idea.codegen.module;

import com.intellij.ide.util.projectWizard.ModuleWizardStep;
import com.intellij.openapi.options.ConfigurationException;
import top.imyzt.learning.plugin.idea.codegen.domain.model.vo.ProjectConfig;
import top.imyzt.learning.plugin.idea.codegen.infrastructure.DataSetting;
import top.imyzt.learning.plugin.idea.codegen.ui.ProjectConfigUI;

import javax.swing.*;

/**
 * @author imyzt
 * @date 2021/12/26
 * @description 描述信息
 */
public class DDDModuleConfigStep extends ModuleWizardStep {

    private ProjectConfigUI projectConfigUI;

    public DDDModuleConfigStep(ProjectConfigUI projectConfigUI) {
        this.projectConfigUI = projectConfigUI;
    }

    @Override
    public JComponent getComponent() {
        return projectConfigUI.getProjectInfoPanel();
    }

    @Override
    public void updateDataModel() {

    }

    @Override
    public boolean validate() throws ConfigurationException {

        // 获取配置信息, 写入DataSetting

        ProjectConfig projectConfig = DataSetting.getInstance().getProjectConfig();

        projectConfig.set_groupId(projectConfig.get_groupId());
        projectConfig.set_artifactId(projectConfig.get_artifactId());
        projectConfig.set_version(projectConfig.get_version());
        projectConfig.set_package(projectConfig.get_package());


        return super.validate();
    }
}