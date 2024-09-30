package top.imyzt.learning.spi;

import java.util.List;

/**
 * @author imyzt
 * @date 2024/09/30
 * @description 文本搜索接口
 */
public interface TextSearch {

    /**
     * 搜索文本
     * @param text 搜索内容
     * @return 搜索结果
     */
    List<String> search(String text);
}
