package top.imyzt.learning.plugin.idea.codegen.domain.service;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.twelvemonkeys.lang.StringUtil;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.jetbrains.annotations.Nullable;
import top.imyzt.learning.plugin.idea.codegen.domain.model.vo.ProjectConfig;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;

/**
 * @author imyzt
 * @date 2021/12/26
 * @description 描述信息
 */
public abstract class AbstractProjectGenerator extends FreemarkerConfiguration implements IProjectGenerator {

    @Override
    public void doGenerator(Project project, String entryPath, ProjectConfig projectConfig) {

        // 1. 创建工程主POM
        generateProjectPOM(project, entryPath, projectConfig);

        // 2. 创建四层架构
        generateProjectDDD(project, entryPath, projectConfig);

        // 3. 创建Application

        generateProjectApplication(project, entryPath, projectConfig);

        // 4. 创建YAML
        generateProjectYaml(project, entryPath, projectConfig);

        // 5. 创建Common
        generateProjectCommon(project, entryPath, projectConfig);


    }

    protected abstract void generateProjectCommon(Project project, String entryPath, ProjectConfig projectConfig);

    protected abstract void generateProjectYaml(Project project, String entryPath, ProjectConfig projectConfig);

    protected abstract void generateProjectApplication(Project project, String entryPath, ProjectConfig projectConfig);

    protected abstract void generateProjectDDD(Project project, String entryPath, ProjectConfig projectConfig);

    protected abstract void generateProjectPOM(Project project, String entryPath, ProjectConfig projectConfig);

    protected final void writeFile(Project project, String packageName, String entryPath, String name, String ftl, Object dataModel) {

        VirtualFile virtualFile = createPackageDir(packageName, entryPath);

        StringWriter stringWriter = new StringWriter();
        try {
            Template template = super.getTemplate(ftl);
            template.process(dataModel, stringWriter);
            virtualFile.setBinaryContent(stringWriter.toString().getBytes(StandardCharsets.UTF_8));
        } catch (IOException | TemplateException e) {
            e.printStackTrace();
        }


    }

    @Nullable
    private VirtualFile createPackageDir(String packageName, String entryPath) {
        String path = FileUtil.toSystemIndependentName(entryPath + "/" + StringUtil.replace(packageName, ".", "/"));
        new File(path).mkdirs();
        return LocalFileSystem.getInstance().refreshAndFindFileByPath(path);
    }
}