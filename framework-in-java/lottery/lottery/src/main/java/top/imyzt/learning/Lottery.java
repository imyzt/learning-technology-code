package top.imyzt.learning;


import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

/**
 * @author imyzt
 * @date 2025/05/26
 * @description 描述信息
 */
@Slf4j
public final class Lottery {

    private final TreeMap<BigDecimal, Integer> weightMap = new TreeMap<>();
    private BigDecimal totalWeight = BigDecimal.ZERO;
    private static final int SCALE = 10; // 支持亿分之一概率

    public Lottery(List<Prize> prizes) {
        prizes = validPrizes(prizes);

        for (Prize prize : prizes) {
            // 保证传入概率为百分比：0.01 ~ 100.00
            BigDecimal probability = prize.getProbability();
            totalWeight = totalWeight.add(probability);
            weightMap.put(totalWeight, prize.getId()); // 区间为 (prev, current]
        }
    }

    public Integer draw() {
        // 生成一个在 [0, totalWeight) 范围内的高精度随机数
        BigDecimal r = BigDecimal.valueOf(ThreadLocalRandom.current().nextDouble())
                .multiply(totalWeight)
                .setScale(SCALE, RoundingMode.FLOOR);
        Map.Entry<BigDecimal, Integer> entry = weightMap.ceilingEntry(r);
        if (entry == null) {
            throw new IllegalStateException("抽奖失败，未找到合适奖品。随机值: " + r);
        }
        return entry.getValue();
    }

    private List<Prize> validPrizes(List<Prize> prizes) {
        if (prizes == null || prizes.isEmpty()) {
            throw new IllegalArgumentException("奖品不能为空");
        }

        prizes = prizes.stream()
                .filter(p -> {
                    BigDecimal prob = p.getProbability();
                    if (prob.compareTo(BigDecimal.valueOf(0)) < 0 || prob.compareTo(BigDecimal.valueOf(100)) > 0) {
                        throw new IllegalArgumentException(String.format("非法概率, 奖品ID=%d, 概率=%s", p.getId(), prob));
                    }
                    return true;
                })
                .filter(p -> p.getStock() > 0)
                .collect(Collectors.toList());

        BigDecimal sum = prizes.stream()
                .map(Prize::getProbability)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        if (sum.compareTo(BigDecimal.valueOf(100)) > 0) {
            throw new IllegalArgumentException("总概率不能超过100%");
        } else if (sum.compareTo(BigDecimal.valueOf(100)) < 0) {
            Prize miss = new Prize();
            miss.setId(Prize.MISS_PRIZE_ID);
            miss.setName("未中奖");
            miss.setProbability(BigDecimal.valueOf(100).subtract(sum));
            prizes.add(miss);
        }

        return prizes;
    }

}
