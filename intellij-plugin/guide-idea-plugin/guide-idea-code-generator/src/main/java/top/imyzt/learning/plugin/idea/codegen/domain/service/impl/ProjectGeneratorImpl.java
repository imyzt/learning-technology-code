package top.imyzt.learning.plugin.idea.codegen.domain.service.impl;

import com.intellij.openapi.project.Project;
import top.imyzt.learning.plugin.idea.codegen.domain.model.vo.ProjectConfig;
import top.imyzt.learning.plugin.idea.codegen.domain.service.AbstractProjectGenerator;

/**
 * @author imyzt
 * @date 2021/12/26
 * @description 描述信息
 */
public class ProjectGeneratorImpl extends AbstractProjectGenerator {


    @Override
    protected void generateProjectCommon(Project project, String entryPath, ProjectConfig projectConfig) {
        writeFile(project, "src/main/java/", projectConfig.get_package() + "/common","Constants.java", "common/Constants.ftl", projectConfig);
    }

    @Override
    protected void generateProjectYaml(Project project, String entryPath, ProjectConfig projectConfig) {
        writeFile(project, "src/main/resource/", entryPath, "application.yml", "yml.ftl", projectConfig);
    }

    @Override
    protected void generateProjectApplication(Project project, String entryPath, ProjectConfig projectConfig) {
        writeFile(project, "src/main/java/", projectConfig.get_package(),projectConfig.get_artifactId() + "Application.java", "application.ftl", projectConfig);
    }

    @Override
    protected void generateProjectDDD(Project project, String entryPath, ProjectConfig projectConfig) {

        // create application
        writeFile(project, "src/main/java/" + projectConfig.get_package() + ".application", entryPath, "package-info.java", "application/package-info.ftl", projectConfig);

        // create common
        writeFile(project, "src/main/java/" + projectConfig.get_package() + ".common", entryPath, "package-info.java", "common/package-info.ftl", projectConfig);

        // create domain
        writeFile(project, "src/main/java/" + projectConfig.get_package() + ".domain", entryPath, "package-info.java", "domain/package-info.ftl", projectConfig);

        // create infrastructure
        writeFile(project, "src/main/java/" + projectConfig.get_package() + ".infrastructure", entryPath, "package-info.java", "infrastructure/package-info.ftl", projectConfig);

        // create interfaces
        writeFile(project, "src/main/java/" + projectConfig.get_package() + ".interfaces", entryPath, "package-info.java", "interfaces/package-info.ftl", projectConfig);
    }

    @Override
    protected void generateProjectPOM(Project project, String entryPath, ProjectConfig projectConfig) {
        writeFile(project, "/", entryPath, "pom.xml", "pom.ftl", projectConfig);
    }
}