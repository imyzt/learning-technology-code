package top.imyzt.learning.plugin.idea.codegen.module;

import com.intellij.ide.util.projectWizard.ModuleBuilder;
import com.intellij.ide.util.projectWizard.ModuleNameLocationSettings;
import com.intellij.ide.util.projectWizard.ModuleWizardStep;
import com.intellij.ide.util.projectWizard.SettingsStep;
import com.intellij.ide.util.projectWizard.WizardContext;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.application.Result;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.module.ModuleType;
import com.intellij.openapi.module.StdModuleTypes;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.project.DumbService;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ModifiableRootModel;
import com.intellij.openapi.roots.ui.configuration.ModulesProvider;
import com.intellij.openapi.startup.StartupManager;
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.util.DisposeAwareRunnable;
import com.twelvemonkeys.lang.StringUtil;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nls.Capitalization;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.imyzt.learning.plugin.idea.codegen.domain.service.IProjectGenerator;
import top.imyzt.learning.plugin.idea.codegen.domain.service.impl.ProjectGeneratorImpl;
import top.imyzt.learning.plugin.idea.codegen.infrastructure.DataSetting;
import top.imyzt.learning.plugin.idea.codegen.infrastructure.ICONS;
import top.imyzt.learning.plugin.idea.codegen.infrastructure.MsgBundle;
import top.imyzt.learning.plugin.idea.codegen.ui.ProjectConfigUI;

import javax.swing.*;
import java.io.File;
import java.util.Objects;

/**
 * @author imyzt
 * @date 2021/12/26
 * @description 脚手架构建类
 */
public class DDDModuleBuilder extends ModuleBuilder {

    private IProjectGenerator projectGenerator = new ProjectGeneratorImpl();

    @Override
    public Icon getNodeIcon() {
        return ICONS.SPRING_BOOT;
    }

    @Override
    public ModuleType<?> getModuleType() {
        return StdModuleTypes.JAVA;
    }

    @Nls(capitalization = Capitalization.Title)
    @Override
    public String getPresentableName() {
        return "Spring Boot";
    }

    @Nls(capitalization = Capitalization.Sentence)
    @Override
    public String getDescription() {
        return MsgBundle.message("wizard.spring.boot.description");
    }

    @Nullable
    @Override
    public String getBuilderId() {
        return getClass().getName();
    }

    @Nullable
    @Override
    public ModuleWizardStep modifySettingsStep(@NotNull SettingsStep settingsStep) {

        ModuleNameLocationSettings moduleNameLocationSettings = settingsStep.getModuleNameLocationSettings();
        String artifactId = DataSetting.getInstance().getProjectConfig().get_artifactId();
        if (null != moduleNameLocationSettings && !StringUtil.isEmpty(artifactId)) {
            moduleNameLocationSettings.setModuleName(artifactId);
        }

        return super.modifySettingsStep(settingsStep);
    }

    @Override
    public void setupRootModel(@NotNull ModifiableRootModel modifiableRootModel) throws ConfigurationException {

        // set jdk
        if (null != this.myJdk) {
            modifiableRootModel.setSdk(this.myJdk);
        } else {
            modifiableRootModel.inheritSdk();
        }

        // 生成工程路径
        String path = FileUtil.toSystemIndependentName(Objects.requireNonNull(getContentEntryPath()));
        new File(path).mkdirs();
        VirtualFile virtualFile = LocalFileSystem.getInstance().refreshAndFindFileByPath(path);
        modifiableRootModel.addContentEntry(virtualFile);

        Project project = modifiableRootModel.getProject();

        Runnable r = () -> new WriteCommandAction<VirtualFile>(project) {
            @Override
            protected void run(@NotNull Result<VirtualFile> result) throws Throwable {
                projectGenerator.doGenerator(project, getContentEntryPath(), DataSetting.getInstance().getProjectConfig());
            }
        }.execute();

        if (ApplicationManager.getApplication().isUnitTestMode() || ApplicationManager.getApplication().isHeadlessEnvironment()) {
            r.run();
            return;
        }

        if (!project.isInitialized()) {
            StartupManager.getInstance(project).registerPostStartupActivity(DisposeAwareRunnable.create(r, project));
            return;
        }

        if (DumbService.isDumbAware(r)) {
            r.run();
        } else {
            DumbService.getInstance(project).runWhenSmart(DisposeAwareRunnable.create(r, project));
        }

    }

    @Override
    public ModuleWizardStep[] createWizardSteps(@NotNull WizardContext wizardContext, @NotNull ModulesProvider modulesProvider) {

        DDDModuleConfigStep dddModuleConfigStep = new DDDModuleConfigStep(new ProjectConfigUI());

        return new ModuleWizardStep[]{dddModuleConfigStep};
    }
}