package top.imyzt.learning;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) {

        List<Prize> prizes = new ArrayList<>();

        // 奖品1：0.0001%
        prizes.add(new Prize(1, "超级大奖", BigDecimal.valueOf(0.0001), 10));
        // 奖品2：0.01%
        prizes.add(new Prize(2, "稀有奖", BigDecimal.valueOf(0.01), 100));
        // 奖品3：1%
        prizes.add(new Prize(3, "普通奖", BigDecimal.valueOf(1.0), 1000));
        // 奖品4：10%
        prizes.add(new Prize(4, "常规奖", BigDecimal.valueOf(10.0), 10000));
        // 奖品5：50%
        prizes.add(new Prize(5, "鼓励奖", BigDecimal.valueOf(50.0), 100000));

        Lottery lottery = new Lottery(prizes);

        Map<Integer, Integer> hitCount = new HashMap<>();
        int totalDraws = 1_000_000;

        for (int i = 0; i < totalDraws; i++) {
            Integer prizeId = lottery.draw();
            hitCount.merge(prizeId, 1, Integer::sum);
        }

        Map<Integer, Prize> prizeMap = prizes.stream().collect(Collectors.toMap(Prize::getId, v -> v));
        // 输出结果
        System.out.println("抽奖总次数: " + totalDraws);
        System.out.println("中奖统计：");
        hitCount.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .forEach(entry -> {
                    int count = entry.getValue();
                    double percentage = count * 100.0 / totalDraws;

                    Prize prize = prizeMap.getOrDefault(entry.getKey(), new Prize(Prize.MISS_PRIZE_ID, "未中奖", BigDecimal.ZERO, 0));
                    System.out.printf("奖品名称=%s \t 次数=%d \t 配置概率=%s%% \t 概率=%.6f%%\n",
                            prize.getName() , count, prize.getProbability(), percentage);
                });
    }

    private static void demo() {
        // 奖品初始化
        Prize prize1 = new Prize();
        prize1.setId(1);
        prize1.setName("奖品1");
        prize1.setProbability(BigDecimal.valueOf(10));
        prize1.setStock(100);

        Prize prize2 = new Prize();
        prize2.setId(2);
        prize2.setName("奖品2");
        prize2.setProbability(BigDecimal.valueOf(20));
        prize2.setStock(200);

        Prize prize3 = new Prize();
        prize3.setId(3);
        prize3.setName("奖品3");
        prize3.setProbability(BigDecimal.valueOf(30));
        prize3.setStock(0);

        Prize prize4 = new Prize();
        prize4.setId(4);
        prize4.setName("奖品4");
        prize4.setProbability(BigDecimal.valueOf(0.12));
        prize4.setStock(400);

        Prize prize5 = new Prize();
        prize5.setId(5);
        prize5.setName("奖品5");
        prize5.setProbability(BigDecimal.valueOf(0.01));
        prize5.setStock(500);

        List<Prize> prizes = new ArrayList<>();
        prizes.add(prize1);
        prizes.add(prize2);
        prizes.add(prize3);
        prizes.add(prize4);
        prizes.add(prize5);

        Map<Integer, Prize> prizeMap = prizes.stream().collect(Collectors.toMap(Prize::getId, prize -> prize));

        // 1. 检查,并核销抽奖机会
        // 2. 查询奖品列表
        // 3. 过滤该用户已达限制的奖品, 过滤无库存奖品
        // 4. 抽奖
        Lottery lottery = new Lottery(prizes);
        // 5. 扣减库存(存在失败的情况), 此时分配[未中奖]奖品
        // 6. 记录奖池ids, 记录中奖奖品ID

        for (int j = 0; j < 10; j++) {
            Map<String, Integer> prizeCountMap = new TreeMap<>();
            for (int i = 0; i < 10000*j; i++) {
                Integer prizeId = lottery.draw();
                String prizeName;
                if (Objects.equals(prizeId, Prize.MISS_PRIZE_ID)) {
                    prizeName = "未中奖";
                } else {
                    prizeName = prizeMap.get(prizeId).getName();
                }
                prizeCountMap.put(prizeName, prizeCountMap.getOrDefault(prizeName, 0) + 1);
                // System.out.println(prizeName);
            }
            System.out.println("总抽奖次数" + 10000*j + ":" + prizeCountMap);
        }
    }


}
