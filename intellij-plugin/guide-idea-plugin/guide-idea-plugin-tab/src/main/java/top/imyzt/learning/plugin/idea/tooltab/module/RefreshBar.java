package top.imyzt.learning.plugin.idea.tooltab.module;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.DumbAwareAction;
import com.intellij.openapi.util.IconLoader;
import org.jetbrains.annotations.NotNull;
import top.imyzt.learning.plugin.idea.tooltab.infrastructure.DataSetting;

/**
 * @author imyzt
 * @date 2021/11/30
 * @description 工具栏刷新按钮
 */
public class RefreshBar extends DumbAwareAction {

    private ViewBars panel;

    public RefreshBar(ViewBars panel) {
        super("刷新指数", "Click to refresh", IconLoader.getIcon("/icons/refresh.svg"));
        this.panel = panel;
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        panel.getConsoleUI().addRows(DataSetting.getInstance().getGids());
    }
}