package top.imyzt.learning.security.browser.support;

import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;

/**
 * @author imyzt
 * @date 2019/6/6
 * @description 简单返回对象
 */
@Data
@RequiredArgsConstructor(staticName = "of")
public class SimpleResponse implements Serializable {

    @NonNull
    private Object content;
}
