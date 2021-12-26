package top.imyzt.learning.plugin.idea.codegen.infrastructure;

import com.intellij.openapi.util.IconLoader;

import javax.swing.*;

/**
 * @author imyzt
 * @date 2021/12/26
 * @description 描述信息
 */
public class ICONS {

    private static Icon load(String path) {
        return IconLoader.getIcon(path, ICONS.class);
    }

    public static final Icon DDD = load("/icons/DDD.png");

    public static final Icon SPRING_BOOT = load("/icons/spring-boot.png");
}