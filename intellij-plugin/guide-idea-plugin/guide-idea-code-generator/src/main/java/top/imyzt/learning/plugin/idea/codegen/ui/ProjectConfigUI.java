package top.imyzt.learning.plugin.idea.codegen.ui;

import lombok.Getter;

import javax.swing.*;

/**
 * @author imyzt
 * @date 2021/12/26
 * @description 项目信息配置页
 */
@Getter
public class ProjectConfigUI  {
    private JTextField groupIdValue;
    private JTextField artifactIdValue;
    private JTextField versionValue;
    private JTextField textField4;
    private JLabel groupId;
    private JLabel projectInfo;
    private JLabel artifactId;
    private JLabel version;
    private JLabel packageField;
    private JPanel projectInfoPanel;
}