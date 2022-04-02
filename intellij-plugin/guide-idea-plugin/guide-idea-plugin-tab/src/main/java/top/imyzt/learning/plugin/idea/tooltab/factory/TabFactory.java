package top.imyzt.learning.plugin.idea.tooltab.factory;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import com.intellij.ui.content.ContentFactory.SERVICE;
import org.jetbrains.annotations.NotNull;
import top.imyzt.learning.plugin.idea.tooltab.module.ViewBars;

/**
 * @author imyzt
 * @date 2021/11/30
 * @description 窗体工厂
 */
public class TabFactory implements ToolWindowFactory {

    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {

        // 窗体
        ViewBars viewBars = new ViewBars(project);

        // 获取内容工厂的实例
        ContentFactory contentFactory = SERVICE.getInstance();

        // 获取ToolWindow显示的内容
        Content content = contentFactory.createContent(viewBars, "股票", false);
        // 设置ToolWindow显示的内容
        toolWindow.getContentManager().addContent(content, 0);


    }
}