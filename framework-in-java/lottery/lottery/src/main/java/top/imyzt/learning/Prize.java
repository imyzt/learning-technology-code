package top.imyzt.learning;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Prize {

    public static final Integer MISS_PRIZE_ID = -1;

    /** 奖品ID */
    private Integer id;
    /** 奖品名称 */
    private String name;
    /** 概率 */
    private BigDecimal probability;
    /** 库存数量 */
    private Integer stock;
}
