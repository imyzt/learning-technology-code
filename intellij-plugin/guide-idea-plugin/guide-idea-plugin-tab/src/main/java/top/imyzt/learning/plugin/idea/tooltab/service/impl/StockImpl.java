package top.imyzt.learning.plugin.idea.tooltab.service.impl;

import com.alibaba.fastjson.JSON;
import top.imyzt.learning.plugin.idea.tooltab.model.aggregates.StockResult;
import top.imyzt.learning.plugin.idea.tooltab.model.vo.Data;
import top.imyzt.learning.plugin.idea.tooltab.model.vo.GoPicture;
import top.imyzt.learning.plugin.idea.tooltab.model.vo.Stock;
import top.imyzt.learning.plugin.idea.tooltab.service.IStock;

import java.util.ArrayList;
import java.util.List;

/**
 * @author imyzt
 * @date 2021/11/30
 * @description 股票查询实现类
 */
public class StockImpl implements IStock {

    @Override
    public List<Data> queryPresetStockData(List<String> gids) {
        ArrayList<Data> dataList = new ArrayList<>();
        for (String gid : gids) {
            StockResult stockResult = JSON.parseObject(get(gid), StockResult.class);
            Stock[] stocks = stockResult.getResult();
            for (Stock stock : stocks) {
                dataList.add(stock.getData());
            }
        }
        return dataList;
    }

    @Override
    public GoPicture queryGidGoPicture(String gid) {
        StockResult stockResult = JSON.parseObject(get(gid), StockResult.class);
        Stock[] stocks = stockResult.getResult();
        return stocks[0].getGoPicture();
    }

    private String get(String gid) {
        return "{\"resultcode\":\"200\",\"reason\":\"SUCCESSED!\",\"result\":[{\"data\":{\"buyFive\":\"97800\",\"buyFivePri\":\"36.750\",\"buyFour\":\"96400\",\"buyFourPri\":\"36.760\",\"buyOne\":\"83800\",\"buyOnePri\":\"36.790\",\"buyThree\":\"12600\",\"buyThreePri\":\"36.770\",\"buyTwo\":\"12846\",\"buyTwoPri\":\"36.780\",\"competitivePri\":\"36.790\",\"date\":\"2021-11-17\",\"gid\":\"sz000651\",\"increPer\":\"1.29\",\"increase\":\"0.47\",\"name\":\"格力电器\",\"nowPri\":\"36.790\",\"reservePri\":\"36.800\",\"sellFive\":\"25600\",\"sellFivePri\":\"36.880\",\"sellFour\":\"6900\",\"sellFourPri\":\"36.870\",\"sellOne\":\"100\",\"sellOnePri\":\"36.800\",\"sellThree\":\"8800\",\"sellThreePri\":\"36.860\",\"sellTwo\":\"13100\",\"sellTwoPri\":\"36.850\",\"time\":\"09:30:00\",\"todayMax\":\"36.850\",\"todayMin\":\"36.780\",\"todayStartPri\":\"36.780\",\"traAmount\":\"34512133.120\",\"traNumber\":\"9382\",\"yestodEndPri\":\"36.320\"},\"dapandata\":{\"dot\":\"36.79\",\"name\":\"格力电器\",\"nowPic\":\"0.47\",\"rate\":\"1.29\",\"traAmount\":\"3451\",\"traNumber\":\"9382\"},\"gopicture\":{\"minurl\":\"http://image.sinajs.cn/newchart/min/n/sz000651.gif\",\"dayurl\":\"http://image.sinajs.cn/newchart/daily/n/sz000651.gif\",\"weekurl\":\"http://image.sinajs.cn/newchart/weekly/n/sz000651.gif\",\"monthurl\":\"http://image.sinajs.cn/newchart/monthly/n/sz000651.gif\"}}],\"error_code\":0}";
    }
}