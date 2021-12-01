package top.imyzt.learning.plugin.idea.tooltab.infrastructure;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author imyzt
 * @date 2021/11/30
 * @description 配置存储类, 可持久化
 */
@Data
public class DataState {

    private List<String> gids = new ArrayList<>();

}