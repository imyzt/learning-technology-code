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

    private final ProjectConfigUI projectConfigUI;

    public DDDModuleConfigStep(ProjectConfigUI projectConfigUI) {
        this.projectConfigUI = projectConfigUI;
    }

    @Override
    public JComponent getComponent() {
        return projectConfigUI.getMainPanel();
    }

    @Override
    public void updateDataModel() {

    }

    @Override
    public boolean validate() throws ConfigurationException {

        // 获取配置信息, 写入DataSetting

        ProjectConfig projectConfig = DataSetting.getInstance().getProjectConfig();

        projectConfig.set_groupId(projectConfigUI.getGroupIdValue().getText());
        projectConfig.set_artifactId(projectConfigUI.getArtifactIdValue().getText());
        projectConfig.set_version(projectConfigUI.getVersionValue().getText());
        projectConfig.set_package(projectConfigUI.getPackageFieldValue().getText());

        return super.validate();
    }
}