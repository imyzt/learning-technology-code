package top.imyzt.learning.plugin.idea.toolwindow.ui;

import lombok.Getter;

import javax.swing.*;
import java.io.File;

/**
 * @author imyzt
 * @date 2021/11/24
 * @description 配置页面
 */
@Getter
public class SettingUI {

    private JLabel urlLabel;
    private JTextField urlTextField;
    private JButton urlButton;
    private JPanel mainPanel;

    public SettingUI() {

        // 给按钮加一个选择文件的事件
        urlButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            fileChooser.showOpenDialog(mainPanel);
            File selectedFile = fileChooser.getSelectedFile();
            urlTextField.setText(selectedFile.getPath());
        });
    }
}