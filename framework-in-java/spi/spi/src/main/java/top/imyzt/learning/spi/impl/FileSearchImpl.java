package top.imyzt.learning.spi.impl;


import top.imyzt.learning.spi.TextSearch;

import java.util.Collections;
import java.util.List;

/**
 * @author imyzt
 * @date 2024/09/30
 * @description file 搜索
 */
public class FileSearchImpl implements TextSearch {
    @Override
    public List<String> search(String text) {
        return Collections.singletonList("file search result test");
    }
}
