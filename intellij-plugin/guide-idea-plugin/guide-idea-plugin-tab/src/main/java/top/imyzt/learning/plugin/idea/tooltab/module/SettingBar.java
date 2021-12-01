package top.imyzt.learning.plugin.idea.tooltab.module;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.options.ShowSettingsUtil;
import com.intellij.openapi.project.DumbAwareAction;
import com.intellij.openapi.util.IconLoader;
import org.jetbrains.annotations.NotNull;
import top.imyzt.learning.plugin.idea.tooltab.ui.GidConfig;

/**
 * @author imyzt
 * @date 2021/11/30
 * @description 描述信息
 */
public class SettingBar extends DumbAwareAction {

    private ViewBars viewBars;

    public SettingBar(ViewBars viewBars) {
        super("配置股票", "Click to setting", IconLoader.getIcon("/icons/config.svg"));
        this.viewBars = viewBars;
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        ShowSettingsUtil.getInstance().editConfigurable(viewBars.getProject(), new GidConfig(viewBars.getConsoleUI()));
    }
}