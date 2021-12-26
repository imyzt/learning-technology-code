package top.imyzt.learning.plugin.idea.codegen.factory;

import com.intellij.ide.util.projectWizard.WizardContext;
import com.intellij.platform.ProjectTemplate;
import com.intellij.platform.ProjectTemplatesFactory;
import com.intellij.platform.templates.BuilderBasedTemplate;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.imyzt.learning.plugin.idea.codegen.infrastructure.ICONS;
import top.imyzt.learning.plugin.idea.codegen.module.DDDModuleBuilder;

import javax.swing.*;

/**
 * @author imyzt
 * @date 2021/12/26
 * @description 描述信息
 */
public class TemplateFactory extends ProjectTemplatesFactory {

    @NotNull
    @Override
    public String[] getGroups() {
        return new String[]{"DDD脚手架"};
    }

    @Override
    public Icon getGroupIcon(String group) {
        return ICONS.DDD;
    }

    @NotNull
    @Override
    public ProjectTemplate[] createTemplates(@Nullable String group, WizardContext context) {
        return new ProjectTemplate[]{new BuilderBasedTemplate(new DDDModuleBuilder())};
    }
}