package top.imyzt.learning.plugin.idea.tooltab.service;

import top.imyzt.learning.plugin.idea.tooltab.model.vo.Data;
import top.imyzt.learning.plugin.idea.tooltab.model.vo.GoPicture;

import java.util.List;

/**
 * @author imyzt
 * @date 2021/11/30
 * @description 查询股票信息的抽象接口
 */
public interface IStock {

    List<Data> queryPresetStockData(List<String> gids);

    GoPicture queryGidGoPicture(String gid);
}
