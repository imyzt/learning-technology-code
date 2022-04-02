package top.imyzt.learning.plugin.idea.tooltab.ui;

import com.intellij.openapi.options.Configurable;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nls.Capitalization;
import org.jetbrains.annotations.Nullable;
import top.imyzt.learning.plugin.idea.tooltab.infrastructure.DataSetting;

import javax.swing.*;
import java.util.Arrays;
import java.util.List;

/**
 * @author imyzt
 * @date 2021/11/30
 * @description 配置窗体
 */
public class GidConfig implements Configurable {

    private JPanel mainPanel;
    private JPanel settingPanel;
    private JTextField gidTextField;
    private JLabel gidLabel;

    private ConsoleUI consoleUI;

    public GidConfig(ConsoleUI consoleUI) {
        this.consoleUI = consoleUI;
    }

    @Nls(capitalization = Capitalization.Title)
    @Override
    public String getDisplayName() {
        return "Stock";
    }

    @Nullable
    @Override
    public JComponent createComponent() {
        return mainPanel;
    }

    @Override
    public boolean isModified() {
        return true;
    }

    @Override
    public void apply() {

        List<String> gidList = DataSetting.getInstance().getGids();
        gidList.clear();

        String[] gids = gidTextField.getText().trim().split(",");
        gidList.addAll(Arrays.asList(gids));

        // 刷新数据
        consoleUI.addRows(gidList);

    }
}