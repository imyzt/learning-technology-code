package top.imyzt.learning.plugin.idea.tooltab.ui;

import lombok.Data;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;

/**
 * @author imyzt
 * @date 2021/11/30
 * @description K线页面
 */
@Data
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

    public ConsoleUI() {
        table.setModel(defaultTableModel);
    }

    public void addRows(List<String> gids) {



    }
}