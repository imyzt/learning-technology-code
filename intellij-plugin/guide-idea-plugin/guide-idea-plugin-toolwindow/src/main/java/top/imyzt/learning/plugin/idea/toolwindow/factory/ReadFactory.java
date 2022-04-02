package top.imyzt.learning.plugin.idea.toolwindow.factory;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import com.intellij.ui.content.ContentFactory.SERVICE;
import org.jetbrains.annotations.NotNull;
import top.imyzt.learning.plugin.idea.toolwindow.tools.Config;
import top.imyzt.learning.plugin.idea.toolwindow.ui.ReadUI;

/**
 * @author imyzt
 * @date 2021/11/24
 * @description 阅读器窗口配置在IDEA的ToolWindow视区
 */
public class ReadFactory implements ToolWindowFactory {

    private ReadUI readUI = new ReadUI();

    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {

        // 获取内容工厂的实例
        ContentFactory contentFactory = SERVICE.getInstance();

        // 获取ToolWindow显示的内容
        Content content = contentFactory.createContent(readUI.getMainPanel(), "", false);

        // 设置ToolWindow显示的内容
        toolWindow.getContentManager().addContent(content);

        // 存储全局
        Config.readUI = readUI;
    }
}