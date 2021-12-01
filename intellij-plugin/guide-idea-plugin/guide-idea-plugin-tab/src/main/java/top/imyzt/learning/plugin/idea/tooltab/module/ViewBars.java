package top.imyzt.learning.plugin.idea.tooltab.module;

import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.ActionToolbar;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.SimpleToolWindowPanel;
import com.intellij.ui.JBSplitter;
import lombok.Getter;
import top.imyzt.learning.plugin.idea.tooltab.ui.ConsoleUI;

/**
 * @author imyzt
 * @date 2021/11/30
 * @description 工具栏, 窗体面板
 */
@Getter
public class ViewBars extends SimpleToolWindowPanel {

    private Project project;
    private ConsoleUI consoleUI;

    public ViewBars(Project project) {
        super(false, true);
        this.project = project;
        consoleUI = new ConsoleUI();

        // 设置窗体侧边栏按钮
        DefaultActionGroup defaultActionGroup = new DefaultActionGroup();
        defaultActionGroup.add(new SettingBar(this));
        defaultActionGroup.add(new RefreshBar(this));

        ActionToolbar toolbar = ActionManager.getInstance().createActionToolbar("bar", defaultActionGroup, false);
        toolbar.setTargetComponent(this);
        setToolbar(toolbar.getComponent());

        // 添加
        JBSplitter jbSplitter = new JBSplitter(false);
        jbSplitter.setSplitterProportionKey("main.splitter.key");
        jbSplitter.setFirstComponent(consoleUI.getPanel1());
        jbSplitter.setProportion(0.3f);
        setContent(jbSplitter);
    }
}