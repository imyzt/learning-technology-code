package top.imyzt.learning.spi.impl;


import top.imyzt.learning.spi.TextSearch;

import java.util.Collections;
import java.util.List;

/**
 * @author imyzt
 * @date 2024/09/30
 * @description office word搜索
 */
public class WordSearchImpl implements TextSearch {
    @Override
    public List<String> search(String text) {
        return Collections.singletonList("word search result test");
    }
}
