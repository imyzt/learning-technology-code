package top.imyzt.learning.plugin.idea.codegen.infrastructure;

import com.intellij.CommonBundle;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.PropertyKey;

import java.util.ResourceBundle;

/**
 * @author imyzt
 * @date 2021/12/26
 * @description 描述信息
 */
public class MsgBundle {

    @NonNls
    private static final String BUNDLE_NAME = "messages.MsgBundle";

    private static final ResourceBundle BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME);

    public static String message(@PropertyKey(resourceBundle = BUNDLE_NAME) String key, Object ... params){
        return CommonBundle.message(BUNDLE, key, params);
    }
}