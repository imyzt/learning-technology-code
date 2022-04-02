# 插件开发常用接口



## SearchableConfigurable



在IDEA中Setting新增栏目配置文件，当点击IDEA的apply时，回调apply方法。

plugin.xml配置：

```
<extensions defaultExtensionNs="com.intellij">
        <!--配置File -> Setting -> Tools -->
        <projectConfigurable groupId="tools" displayName="READ-CONF" id="learning.toolwindow.id"
                             instance="top.imyzt.learning.plugin.idea.toolwindow.factory.SettingFactory"/>

    </extensions>
```



## ToolWindowFactory



在IDEA的侧边栏新增菜单，通过对参数`toolWindow.getContentManager().addContent(content);`新增新的视窗。

plugin.xml配置：

```
<extensions defaultExtensionNs="com.intellij">
        <!--窗体(IDEA界面右侧)-->
        <toolWindow id="READ-CONF" secondary="false" anchor="right" icon="/icons/logo.png"
        factoryClass="top.imyzt.learning.plugin.idea.toolwindow.factory.ReadFactory"/>
    </extensions>
```



## SimpleToolWindowPanel

在IDEA的下边栏新增菜单，通过`super.setContent(jbSplitter);` 新增新的视窗。

```
<extensions defaultExtensionNs="com.intellij">
    <!-- Add your extensions here -->

    <!--anchor="bottom"展示在最下方-->
    <toolWindow id="XUtil"
                canCloseContents="true"
                anchor="bottom"
                factoryClass="top.imyzt.learning.plugin.idea.tooltab.factory.TabFactory"
                icon="/icons/stock.png"
                />

</extensions>
```



## DumbAwareAction 

配置展示SimpleToolWindowPanel上面的小按钮。



SimpleToolWindowPanel与DumbAwareAction完整示例。

```
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
        super.setContent(jbSplitter);
    }
}
```





## 数据存储PersistentStateComponent



1. 通过对实现了 `PersistentStateComponent`  接口的类添加注解`@State(name = "DataSetting",storages = @Storage("plugin.xml"))`，即可指定本类的属性持久化到数据配置中。

2. plugin.xml指定。

```
<extensions defaultExtensionNs="com.intellij">
    <applicationService serviceImplementation="top.imyzt.learning.plugin.idea.tooltab.infrastructure.DataSetting"/>
</extensions>

```



