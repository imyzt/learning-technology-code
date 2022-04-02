package top.imyzt.learning.plugin.idea.toolwindow.factory;

import com.intellij.openapi.options.SearchableConfigurable;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.imyzt.learning.plugin.idea.toolwindow.tools.Config;
import top.imyzt.learning.plugin.idea.toolwindow.ui.SettingUI;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;

/**
 * @author imyzt
 * @date 2021/11/24
 * @description 阅读器配置在IDEA设置面板的位置
 */
@Slf4j
public class SettingFactory implements SearchableConfigurable {

    private SettingUI settingUI = new SettingUI();

    @Override
    public @NotNull
    @NonNls String getId() {
        return "learning.toolwindow.id";
    }

    @Override
    public String getDisplayName() {
        return "learning.toolwindow.config";
    }

    @Override
    public @Nullable JComponent createComponent() {
        return settingUI.getMainPanel();
    }

    @Override
    public boolean isModified() {
        return true;
    }

    /**
     * 当配置类的apply按下的时候回调
     */
    @Override
    public void apply() {
        String url = settingUI.getUrlTextField().getText();

        File file = new File(url);
        RandomAccessFile raf = null;
        try {
            raf = new RandomAccessFile(file, "r");
            raf.seek(0);

            byte[] bytes = new byte[1024 * 1024];
            int readSize = raf.read(bytes);

            byte[] copy = new byte[readSize];
            System.arraycopy(bytes, 0, copy, 0, readSize);

            String filePathStr = new String(copy, StandardCharsets.UTF_8);

            Config.readUI.getTextContent().setText(filePathStr);
        } catch (IOException e) {
            log.error("readFileFail", e);
        }

    }
}