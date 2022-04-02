package top.imyzt.learning.plugin.idea.tooltab.infrastructure;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * @author imyzt
 * @date 2021/11/30
 * @description 存储配置类, 可将信息持久化
 */
@Slf4j
@State(name = "DataSetting",storages = @Storage("plugin.xml"))
public class DataSetting implements PersistentStateComponent<DataState> {

    private DataState dataState = new DataState();

    public static DataSetting getInstance() {
        return ServiceManager.getService(DataSetting.class);
    }

    @Nullable
    @Override
    public DataState getState() {
        return dataState;
    }

    @Override
    public void loadState(@NotNull DataState state) {
        this.dataState = state;
        log.info("load DataState, detail={}", state);
    }

    public List<String> getGids() {
        return dataState.getGids();
    }
}