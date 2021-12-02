package top.imyzt.learning.plugin.idea.tooltab.ui;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import top.imyzt.learning.plugin.idea.tooltab.infrastructure.DataSetting;
import top.imyzt.learning.plugin.idea.tooltab.model.vo.GoPicture;
import top.imyzt.learning.plugin.idea.tooltab.service.IStock;
import top.imyzt.learning.plugin.idea.tooltab.service.impl.StockImpl;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

/**
 * @author imyzt
 * @date 2021/11/30
 * @description K线页面
 */
@Data
@Slf4j
public class ConsoleUI {
    private JTabbedPane tabbedPane;
    private JPanel panel1;
    private JTable table;
    private JLabel picMin;
    private JLabel picDay;
    private JPanel one;
    private JPanel two;

    private static final DefaultTableModel defaultTableModel =
            new DefaultTableModel(new Object[][]{}, new String[]{"股票", "代码", "最新", "涨跌", "涨幅"});

    private IStock stock = new StockImpl();

    public ConsoleUI() {
        table.setModel(defaultTableModel);

        addRows(DataSetting.getInstance().getGids());

        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = table.getSelectedRow();
                Object value = table.getValueAt(row, 1);
                GoPicture goPicture = stock.queryGidGoPicture(value.toString());

                picMin.setSize(545, 300);
                picDay.setSize(545, 300);

                try {
                    picMin.setIcon(new ImageIcon(new URL(goPicture.getMinurl())));
                    picDay.setIcon(new ImageIcon(new URL(goPicture.getDayurl())));
                } catch (MalformedURLException malformedURLException) {
                    log.error("图片加载出错", malformedURLException);
                }
            }
        });
    }

    public void addRows(List<String> gids) {
        List<top.imyzt.learning.plugin.idea.tooltab.model.vo.Data> dataList = stock.queryPresetStockData(gids);

        int rowCount = defaultTableModel.getRowCount();
        for (int i = 0; i < rowCount; i++) {
            defaultTableModel.removeRow(0);
        }

        for (top.imyzt.learning.plugin.idea.tooltab.model.vo.Data data : dataList) {
            defaultTableModel.addRow(new String[]{data.getName(), data.getGid(), data.getNowPri(), data.getIncrease(), data.getIncrePer()});
            table.setModel(defaultTableModel);
        }
    }
}