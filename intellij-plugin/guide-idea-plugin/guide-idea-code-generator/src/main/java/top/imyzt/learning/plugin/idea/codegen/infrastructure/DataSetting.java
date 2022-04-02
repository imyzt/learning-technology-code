package top.imyzt.learning.plugin.idea.codegen.infrastructure;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.imyzt.learning.plugin.idea.codegen.domain.model.vo.ProjectConfig;

/**
 * @author imyzt
 * @date 2021/12/26
 * @description 数据存储
 */
@State(name = "DataSetting", storages = @Storage("plugin.xml"))
public class DataSetting implements PersistentStateComponent<DataState> {

    private DataState dataState = new DataState();

    @Override
    public @Nullable DataState getState() {
        return dataState;
    }

    @Override
    public void loadState(@NotNull DataState state) {
        this.dataState = state;
    }

    public static DataSetting getInstance() {
        return ServiceManager.getService(DataSetting.class);
    }

    public ProjectConfig getProjectConfig() {
        return dataState.getProjectConfig();
    }
}